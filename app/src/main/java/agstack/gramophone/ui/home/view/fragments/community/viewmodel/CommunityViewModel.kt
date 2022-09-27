package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.Data
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.POST_ID
import android.app.AlertDialog
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : BaseViewModel<CommunityFragmentNavigator>() {
    private lateinit var menuClickedData: Data
    lateinit var mAlertDialog: AlertDialog
    var sorting = ObservableField<String>()
    var showProgress = ObservableField<Boolean>()
    var limit = ObservableField<Int>()
    lateinit var communityPostAdapter: CommunityPostAdapter

    fun filterPost(v: TabLayout.Tab) {
        showProgress.set(true)
        when (v.text) {
            Constants.POST_LATEST -> {
                sorting.set("latest")
            }
            Constants.POST_TRENDING -> {
                sorting.set("trending")
            }
            Constants.POST_FOLLOWING -> {
                sorting.set("following")
            }
            Constants.POST_EXPERT -> {
                sorting.set("expert")
            }
            Constants.POST_SELF -> {
                sorting.set("self")

            }
            Constants.POST_BOOKMARK -> {
                sorting.set("bookmark")
            }
        }

        loadData(sorting.get()!!)
    }


    fun loadData(sorting: String) {
        limit.set(105)
        viewModelScope.launch(Dispatchers.Default) {
            getPost(sorting)
        }
    }


    fun setDialog(mAlertDialog: AlertDialog?) {
        this.mAlertDialog = mAlertDialog!!
    }

    fun onCancel(){
        mAlertDialog?.dismiss()
    }

    fun onDelete(){
        mAlertDialog?.dismiss()
        deletePost()
    }

    private fun deletePost() {

    }

    fun onReport() {
        mAlertDialog?.dismiss()
    }

    fun onBlock() {
        mAlertDialog?.dismiss()
    }



    private suspend fun getPost(sorting: String) {

        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = communityRepository.getCommunityPost(CommunityRequestModel(sorting,limit.get()))
                if (response.isSuccessful) {
                    val data = response.body()?.data

                    communityPostAdapter =  CommunityPostAdapter(data)

                    getNavigator()?.updatePostList(communityPostAdapter,
                        {//postDetail click
                            getNavigator()?.openActivity(
                                PostDetailsActivity::class.java,
                                Bundle().apply {
                                    putString(POST_ID, it)
                                })
                        },
                        {//likes click
                            getNavigator()?.openActivity(
                                LikedPostUserListActivity::class.java,
                                Bundle().apply {
                                    putString(Constants.POST_ID, it)
                                })
                        },
                        {//comments click
                            getNavigator()?.openCommentDialog()
                        },
                        {//whatsapp/facebook/bookmark click
                            getNavigator()?.sharePost(it)
                        },
                        {//pop menu

                        },
                        {
                            when (it.menu) {
                                Constants.PIN_POST -> {
                                    updatePinPost(it)
                                }

                                Constants.DELETE_POST -> {
                                    menuClickedData = it
                                    getNavigator()?.deletePostDialog()
                                }

                                Constants.REPORT_POST -> {
                                    menuClickedData = it

                                    getNavigator()?.reportPostDialog()
                                }

                                Constants.BLOCK_USER -> {
                                    menuClickedData = it
                                    getNavigator()?.blockUserDialog()
                                }

                                Constants.COPY_POST -> {

                                }
                                Constants.EDIT_POST -> {
                               //TODO will be done once cretae post implemented
                                }
                            }

                        },
                        {
                            likePost(it._id, it.position)
                        },
                        {
                            if (it.bookMarked){
                                bookmarkPost(it,"unbookmark")
                            }else{
                                bookmarkPost(it,"bookmark")
                            }
                        }
                    )
                }
            } else
                getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)


            showProgress.set(false)

        } catch (ex: Exception) {
            showProgress.set(false)
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    private fun updatePinPost(it: Data) {
        viewModelScope.launch {
            var status = "pin"
            if (it.pinned) {
                status = "unpin"
            } else {
                status = "pin"
            }
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.pinPost(PostRequestModel(it._id, status))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        var post = communityPostAdapter.getItem(it.position!!)
                        if (it.equals("pin")) {
                            post?.pinned = true
                        } else {
                            post?.pinned = false
                        }
                        communityPostAdapter.notifyItemChanged(it.position!!)


                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }

    private fun bookmarkPost(postData: Data, bookmark: String) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.bookmarkPost(PostRequestModel(postData._id,bookmark))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        var post = communityPostAdapter.getItem(postData.position!!)
                        if (bookmark.equals("bookmark")){
                            post?.bookMarked = true
                        }else{
                            post?.bookMarked = false
                        }
                        communityPostAdapter.notifyItemChanged(postData.position!!)


                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }

    private fun likePost(_id: String, position: Int?) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.likePost(PostRequestModel(_id,""))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        var post = communityPostAdapter.getItem(position!!)
                        post?.likesCount = data?.likesCount!!
                        post?.liked = data.liked
                        communityPostAdapter.notifyItemChanged(position)

                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }

    }

}