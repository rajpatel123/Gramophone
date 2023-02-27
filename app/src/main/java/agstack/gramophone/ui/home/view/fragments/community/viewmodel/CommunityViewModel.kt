package agstack.gramophone.ui.home.view.fragments.community.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.comments.view.CommentsActivity
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter
import agstack.gramophone.ui.home.view.fragments.CommunityFragmentNavigator
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.AnsweredQuizPollRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.Option
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.*
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.ui.othersporfile.model.CommunityUserPostRequestModel
import agstack.gramophone.ui.othersporfile.model.ProfileData
import agstack.gramophone.ui.othersporfile.view.OtherUserProfileActivity
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.AUTHER_ID
import agstack.gramophone.utils.Constants.AUTHER_UUID
import agstack.gramophone.utils.Constants.POST_ID
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import agstack.gramophone.view.activity.EditPostActivity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.android.material.tabs.TabLayout
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val onBoardingRepository: OnBoardingRepository,
    private val productRepository: ProductRepository
) : BaseViewModel<CommunityFragmentNavigator>() {
    var cropResponse: CropResponse?=null
    var myFavoriteCount: Int? = null
    var quizPoll: List<GpApiResponseData>? = null
    var profileData = ObservableField<ProfileData>()
    var postId: String?=null
    var block = "block"
    private lateinit var menuClickedData: Data
    lateinit var mAlertDialog: AlertDialog
    var sorting = ObservableField<String>()

    var blockStr = ObservableField<String>()
    var phoneNo = ObservableField<String>()
    var hateStr = ObservableField<String>()
    var firm = ObservableField<String>()
    var mandi = ObservableField<String>()
    var harasgStr = ObservableField<String>()
    var unWanted = ObservableField<String>()
    var followers = ObservableField<String>()
    var followeee = ObservableField<String>()
    var securitStr = ObservableField<String>()
    var showProgress = ObservableField<Boolean>()
    var following = ObservableField<Boolean>()
    var menuVisible = ObservableField<Boolean>()
    var isGeneral = ObservableField<Boolean>()
    var limit = ObservableField<Int>()
    lateinit var uuid: String
    lateinit var id: String
    lateinit var redirectionSource: String
    lateinit var communityPostAdapter: CommunityPostAdapter
    lateinit var reportReason: String

    fun filterPost(v: TabLayout.Tab) {
        showProgress.set(true)
        getNavigator()?.hideViews()
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

        val properties = Properties()
        properties.addAttribute(
            "Customer_Id",
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.CUSTOMER_ID
            )!!)
            .addAttribute("Filter",sorting.get())
            .setNonInteractive()
        getNavigator()?.sendMoEngageEvent("KA_Filter_Feed", properties)

        loadData(sorting.get()!!)
    }


    fun loadData(sorting: String) {
        limit.set(99)
        viewModelScope.launch(Dispatchers.Default) {
            getPost(sorting)
        }
    }


    fun setDialog(mAlertDialog: AlertDialog?) {
        this.mAlertDialog = mAlertDialog!!
    }

    fun onCancel() {
        mAlertDialog?.dismiss()
    }

    fun onDelete() {
        mAlertDialog?.dismiss()
        deletePost()
    }

    private fun deletePost() {
        showProgress.set(true)
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {

                    val response = communityRepository.deletePost(menuClickedData._id)
                    showProgress.set(false)
                    if (response.isSuccessful) {
                        getPost(sorting = sorting.get().toString())
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }

                    val properties = Properties()
                    properties.addAttribute(
                        "Customer_Id",
                        SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.CUSTOMER_ID
                        )!!)
                        .addAttribute("Post_ID",menuClickedData._id)
                        .setNonInteractive()
                    getNavigator()?.sendMoEngageEvent("KA_Delete_Post", properties)
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }

    fun getProfile(blockedStatus: Int) {
        if (blockedStatus == 1) {
            blockStr.set("Unblock User")
            block="unblock"
        } else{
            blockStr.set("Block User")
            block="block"

        }
        showProgress.set(true)
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {

                    val response = communityRepository.getProfileData(uuid)
                    showProgress.set(false)
                    if (response.isSuccessful && response.body() != null) {
                        profileData.set(response.body()?.data!!)
                        if (!TextUtils.isEmpty("" + response.body()?.data?.photoUrl))
                            getNavigator()?.setProfileImage("" + response.body()?.data?.photoUrl)

                        following.set(profileData.get()?.following)
                        if (TextUtils.isEmpty(profileData.get()?.firm)) {
                            firm.set("--")
                        } else {
                            firm.set(profileData.get()?.firm)
                        }

                        if (TextUtils.isEmpty(profileData.get()?.mandi)) {
                            mandi.set("--")
                        } else {
                            mandi.set(profileData.get()?.mandi)
                        }

                        mandi.set(profileData.get()?.mandi)
                        phoneNo.set(profileData.get()?.phoneNo)
                        if (profileData.get()?.communityUserType.equals("general")) {
                            isGeneral.set(true)
                        } else {
                            isGeneral.set(false)
                        }
                        followers.set(response.body()?.data!!.totalFollowers.toString())
                        followeee.set(response.body()?.data!!.totalFollowees.toString())
                    } else {
                        isGeneral.set(false)
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }


    fun onReport() {
        mAlertDialog?.dismiss()
        if (!TextUtils.isEmpty(reportReason)) {
            reportPost()

        } else {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.select_report_reason))
        }



    }

    private fun reportPost() {

        if ("admin".equals(menuClickedData.author.communityUserType)) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.can_not_block))
            return
        }
        viewModelScope.launch {
            showProgress.set(true)
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.reportPost(
                        ReportUserRequestModel(
                            reportReason,
                            menuClickedData._id
                        )
                    )
                    showProgress.set(false)
                    if (response.isSuccessful) {
                        getPost(sorting = sorting.get().toString())
                        getNavigator()?.showToast(getNavigator()?.getMessage(R.string.complain_logged))

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("Post_ID",menuClickedData._id)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent("KA_Report_Post", properties)

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }
    }

    fun onBlock() {
        blockUser()
        mAlertDialog?.dismiss()
    }


    private fun blockUser() {
        if ("admin".equals(menuClickedData.author.communityUserType)) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.can_not_block))
            return
        }
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.blockUser(
                        BlockUserRequestModel(
                            "block",
                            menuClickedData.author._id,
                            menuClickedData._id
                        )
                    )
                    getNavigator()?.sendBlockUserMoEngageEvent()
                    if (response.isSuccessful) {
                        getPost(sorting = sorting.get().toString())

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("Customer_ID_Blocked",menuClickedData._id)
                            .setNonInteractive()
                       getNavigator()?.sendMoEngageEvent("KA_Block_User", properties)




                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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


    private suspend fun getPost(sorting: String) {
        showProgress.set(true)

        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = communityRepository.getCommunityPost(
                    CommunityRequestModel(
                        sorting,
                        limit.get(),
                        Constants.API_DATA_LIMITS_IN_ONE_TIME.toInt()

                    )
                )
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    myFavoriteCount = response.body()?.data?.size!!
                    getNavigator()?.stopRefresh()
                    communityPostAdapter = CommunityPostAdapter(data, false,quizPoll,
                    )

                    getNavigator()?.updatePostList(
                        communityPostAdapter,
                        {
                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!
                            )
                                .addAttribute("SharedEntity", it)
                            getNavigator()?.sendMoEngageEvent("KA_Share", properties)
                            getNavigator()?.sharePost(it)
                        },
                        {
                          answerQuizPoll(it)


                        },
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

                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!)
                                .addAttribute("Post_ID",it)
                                .setNonInteractive()
                            getNavigator()?.sendMoEngageEvent("KA_View_Likes_On_Post", properties)
                        },
                        {//comments click
                            getNavigator()?.openActivity(
                                CommentsActivity::class.java,
                                Bundle().apply {
                                    putString(Constants.POST_ID, it)
                                })
                        },
                        {//whatsapp/facebook/bookmark click
                            getNavigator()?.sharePost(it)
                        },
                        {//pop menu

                        },
                        {
                            when (it.menu) {
                                Constants.PIN_POST -> {
                                    menuClickedData = it
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
                                    getNavigator()?.openActivity(
                                        EditPostActivity::class.java,
                                        Bundle().apply {
                                            putString(POST_ID, it._id)
                                        })
                                }
                            }

                        },
                        {
                            showProgress.set(true)
                            likePost(it._id, it.position)

                        },
                        {
                            if (it.bookMarked) {
                                bookmarkPost(it, "unbookmark")
                            } else {
                                bookmarkPost(it, "bookmark")
                            }
                        },
                        {
                            followPost(it)
                        },
                        {

                            if (!it.author.uuid.equals(
                                    SharedPreferencesHelper.instance?.getString(
                                        SharedPreferencesKeys.UUIdKey
                                    )
                                )
                            ) {
                                getNavigator()?.openActivity(
                                    OtherUserProfileActivity::class.java,
                                    Bundle().apply {
                                        putString(POST_ID, it._id)
                                        putString(AUTHER_ID, it.author._id)
                                        putString(AUTHER_UUID, it.author.uuid)
                                        putString(Constants.REDIRECTION_SOURCE, "Community Screen")
                                    })
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

    fun getPostByUUID(uuid: String, id: String) {
        this.uuid = uuid
        this.id = id
        viewModelScope.launch {
            getNavigator()?.onLoading()
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.getCommunityPost(
                        CommunityUserPostRequestModel(
                            limit.get(),
                            uuid,
                            "profile"
                        )
                    )
                    if (response.isSuccessful) {
                        val data = response.body()?.data

                        communityPostAdapter = CommunityPostAdapter(
                            data,
                            true,
                            quizPoll
                        )

                        getNavigator()?.updatePostList(communityPostAdapter,
                            {
                                val properties = Properties()
                                properties.addAttribute(
                                    "Customer_Id",
                                    SharedPreferencesHelper.instance?.getString(
                                        SharedPreferencesKeys.CUSTOMER_ID
                                    )!!
                                )
                                    .addAttribute("SharedEntity", it)
                               getNavigator()?.sendMoEngageEvent("KA_Share", properties)
                                getNavigator()?.sharePost(it)

                            },
                            {
                              answerQuizPoll(it)
                            },
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
                                getNavigator()?.openActivity(
                                    CommentsActivity::class.java,
                                    Bundle().apply {
                                        putString(Constants.POST_ID, it)
                                    })
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
                                if (it.bookMarked) {
                                    bookmarkPost(it, "unbookmark")
                                } else {
                                    bookmarkPost(it, "bookmark")
                                }
                            },
                            {
                                followPost(it)
                            },
                            {

                                if (!it.author.uuid.equals(
                                        SharedPreferencesHelper.instance?.getString(
                                            SharedPreferencesKeys.UUIdKey
                                        )
                                    )
                                ) {
                                    getNavigator()?.openActivity(
                                        OtherUserProfileActivity::class.java,
                                        Bundle().apply {
                                            putString(POST_ID, it._id)
                                            putString(AUTHER_ID, it.author._id)
                                            putString(AUTHER_UUID, it.author.uuid)
                                            putString(Constants.REDIRECTION_SOURCE, "Community Screen")
                                        })
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
    }

    private fun answerQuizPoll(it: Option) {
        showProgress.set(true)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {

                    val response =
                        onBoardingRepository.answerQuiz(AnsweredQuizPollRequestModel(it.question_id,it.option_id))
                    if (response.isSuccessful) {
                        showProgress.set(false)
                        getPost(sorting.get()!!)

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("AnsweredEntity",it.option_id)
                            .addAttribute("Answer",it.answer)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent("KA_Answer_QuizPoll", properties)
                    } else {
                        showProgress.set(false)

                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }

    }

    private fun followPost(it: Data) {
        showProgress.set(true)
        viewModelScope.launch {
            try {
                var action= ""
                var event: String
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.followPost(FollowRequestModel(it.author.uuid))
                    if (response.isSuccessful) {

                        var post = communityPostAdapter.getItem(it.position!!)
                        if (response.body()?.data?.following == true) {
                            post?.following = true
                           event="KA_Follow_User"
                           action="Customer_Id_Followed"
                        } else {
                            post?.following = false
                            event="KA_UnFollow_User"
                            action="Customer_Id_UnFollowed"
                        }
                        communityPostAdapter.notifyItemChanged(it.position!!)

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute(action,it.author.uuid)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent(event, properties)

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                    showProgress.set(false)

                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
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
                        var post = communityPostAdapter.getItem(it.position!!)
                        post?.pinned = status.equals("pin")
                        communityPostAdapter.notifyItemChanged(it.position!!)

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("Post_ID",menuClickedData._id)
                            .addAttribute("Action",status)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent("KA_Pin_Post", properties)
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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
                    val response =
                        communityRepository.bookmarkPost(PostRequestModel(postData._id, bookmark))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        var post = communityPostAdapter.getItem(postData.position!!)
                        if (bookmark.equals("bookmark")) {
                            post?.bookMarked = true
                        } else {
                            post?.bookMarked = false
                        }
                        if (sorting.get().equals("bookmark")) {
                            getPost(sorting.get().toString())
                        } else {
                            communityPostAdapter.notifyItemChanged(postData.position!!)
                        }


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
        showProgress.set(true)
        var post = communityPostAdapter.getItem(position!!)
        if (post?.liked == true){
            post?.likesCount = post?.likesCount!!-1

        }else{
            post?.likesCount = post?.likesCount!!+1

        }
        post?.liked = !post.liked
        communityPostAdapter.notifyItemChanged(position)
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.likePost(PostRequestModel(_id, ""))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        var post = communityPostAdapter.getItem(position!!)
                        post?.likesCount = data?.likesCount!!
                        post?.liked = data.liked
                        communityPostAdapter.notifyItemChanged(position)

                        var action  = if (post?.liked == true) "Like" else "Unlike"
                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("Post_ID",post?._id)
                            .addAttribute("Action",action)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent("KA_LikeUnlike_Post", properties)


                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)

              //  showProgress.set(false)

            } catch (ex: Exception) {
               // showProgress.set(false)

                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }

    }

    fun getReason(checkedId: Int) {
        when (checkedId) {
            R.id.rbHate -> {
                reportReason = hateStr.get().toString()
            }
            R.id.rbHarash -> {
                reportReason = harasgStr.get().toString()

            }
            R.id.rbSecurity -> {
                reportReason = securitStr.get().toString()

            }
            R.id.rbUnwanted -> {
                reportReason = unWanted.get().toString()

            }
        }
    }

    fun onBack() {
        getNavigator()?.finishActivity()
    }

    fun onFollowClicked() {
        followUser(uuid)
    }

    fun onOutSideClick() {
        menuVisible.set(false)
    }

    fun onMenuClick() {
        if (menuVisible.get() == true) {
            menuVisible.set(false)
        } else {
            menuVisible.set(true)

        }
    }

    fun onBlockUserClicked() {
        menuVisible.set(false)
        block(postId, id, block)
    }


    private fun block(postId: String?, id: String, action: String) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.blockUser(BlockUserRequestModel(action, id, postId))
                    if (response.isSuccessful) {
                        if (block.equals("block")) {
                            block = "unblock"
                            blockStr.set("Unblock User")

                        } else {
                            block = "block"
                            blockStr.set("Block User")
                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!)
                                .addAttribute("Customer_ID_Blocked",id)
                                .setNonInteractive()
                            getNavigator()?.sendMoEngageEvent("KA_Block_User", properties)

                        }

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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

    private fun followUser(uuid: String) {
        var action = "Fallow"
        showProgress.set(true)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        communityRepository.followPost(FollowRequestModel(uuid))
                    getNavigator()?.sendEditFollowStatusMoEngageEvent()
                    if (response.isSuccessful && response.body() != null && response.body()!!.data.following) {
                        following.set(true)
                        showProgress.set(false)

                    } else {
                        following.set(false)
                        showProgress.set(false)
                    }
                    val properties = Properties()
                    properties.addAttribute(
                        "Customer_Id",
                        SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.CUSTOMER_ID
                        )!!
                    )
                        .addAttribute("Customer_Id_FollowedUnFollowed", id)
                        .addAttribute("Action", action)
                    getNavigator()?.sendMoEngageEvent("KA_FollowUnFollow_User", properties)


                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }

    }

    fun getQuiz() {
        viewModelScope.launch {
            //delay(3000)
            try {

                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response =
                        onBoardingRepository.getQuiz()
                    if (response.isSuccessful && response.body() != null) {
                        quizPoll = response.body()!!.gp_api_response_data
                    } else {
                        showProgress.set(false)
                    }

                    Log.d("PostCall","true")
                    sorting.get()?.let { loadData(it) }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                Log.d("PostCall","false")

                showProgress.set(false)
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }


        }

    }


}