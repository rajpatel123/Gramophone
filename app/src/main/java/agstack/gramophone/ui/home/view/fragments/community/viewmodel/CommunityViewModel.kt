package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.adapter.LikedUsersAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.LikedUsersRequestModel
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.utils.Constants
import android.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : BaseViewModel<CommunityFragmentNavigator>() {
    lateinit var mAlertDialog: AlertDialog
    var sorting  = ObservableField<String>()
    var limit    = ObservableField<Int>()

    fun loadData() {
        sorting.set("trending")
        limit.set(100)
        viewModelScope.launch(Dispatchers.Default) {
        getPost()
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
    }

    fun onReport() {
        mAlertDialog?.dismiss()
    }

    fun onBlock() {
        mAlertDialog?.dismiss()
    }



    private suspend fun getPost() {

        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = communityRepository.getCommunityPost(CommunityRequestModel(sorting.get(),limit.get()))
                if (response.isSuccessful) {
                    val data =  response.body()?.data
                    getNavigator()?.updatePostList(CommunityPostAdapter(data),
                        {//postDetail click
                            getNavigator()?.openActivity(PostDetailsActivity::class.java, null)
                        },
                        {//likes click
                            getNavigator()?.openActivity(LikedPostUserListActivity::class.java, null)
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
                            when (it) {
                                Constants.PIN_POST -> {

                                }

                                Constants.DELETE_POST -> {
                                    getNavigator()?.deletePostDialog()
                                }

                                Constants.REPORT_POST -> {
                                    getNavigator()?.reportPostDialog()
                                }

                                Constants.BLOCK_USER -> {
                                    getNavigator()?.blockUserDialog()
                                }

                                Constants.COPY_POST -> {

                                }
                                Constants.EDIT_POST -> {

                                }
                            }

                        }
                    )
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