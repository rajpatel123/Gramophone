package agstack.gramophone.ui.postdetails.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.FollowRequestModel
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.model.Data
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
 private val  communityRepository: CommunityRepository
) : BaseViewModel<PostDetailNavigator>() {

 private var data: Data? = null
 var authorName = ObservableField<String>()
 var authorLocation = ObservableField<String>()
 var postDate = ObservableField<String>()
 var postDesc = ObservableField<String>()
 var likeCount = ObservableField<String>()
 var commentCount = ObservableField<String>()
 var imageAvailable = ObservableField<Boolean>()

 fun getPostDetails(postId:String) {
 viewModelScope.launch {
  getNavigator()?.onLoading()
  try {
   if (getNavigator()?.isNetworkAvailable() == true) {
    val response = communityRepository.getPostDetails(postId)
    if (response.isSuccessful) {
     data = response.body()?.data
     authorName.set(data?.author?.username)
     // authorLocation.set(data?.author)
     if (data?.description != null)
      postDesc.set(data?.description.toString())

     likeCount.set(data?.likesCount.toString() +" "+getNavigator()?.getMessage(R.string.like))
     commentCount.set(data?.commentsCount.toString()+" "+getNavigator()?.getMessage(R.string.comment_count))
     if (data?.liked == true) {
      getNavigator()?.setLikeImage(R.drawable.ic_liked)
     } else {
      getNavigator()?.setLikeImage(R.drawable.ic_like)
     }

     if (data?.bookMarked == true) {
      getNavigator()?.setBookMarkImage(R.drawable.ic_bookmarked)
     } else {
      getNavigator()?.setBookMarkImage(R.drawable.ic_bookmark)
     }
     if (data?.images?.size!! > 0 && data?.images!![0].url != null) {
      getNavigator()?.onImageSet(data?.images!![0].url)
      imageAvailable.set(true)
     } else {
      imageAvailable.set(false)
     }
    }else{
     getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
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
 fun getPostComments(postId:String) {

 viewModelScope.launch {
  getNavigator()?.onLoading()
  try {
   if (getNavigator()?.isNetworkAvailable() == true) {
    val response = communityRepository.getPostComments(PostRequestModel(postId,""))
    if (response.isSuccessful) {
     val data = response.body()?.data
     if (data != null) {
      val commentsAdapter = CommentsAdapter(data)
      getNavigator()?.updatePostList(commentsAdapter){
       //TODO
      }

     }

    }else{
     getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
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

 fun bookmarkPost() {
  viewModelScope.launch {
   var bookmark = "bookmark"
   if (data?.bookMarked == true) {
    bookmark = "unbookmark"
   } else {
    bookmark = "bookmark"
   }
   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     val response = communityRepository.bookmarkPost(PostRequestModel(data?._id!!, bookmark))
     if (response.isSuccessful) {
      getPostDetails(data?._id!!)
     }else{
      getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
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

 fun likePost() {
  viewModelScope.launch {

   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     val response = communityRepository.likePost(PostRequestModel(data!!._id, ""))
     if (response.isSuccessful) {
      getPostDetails(data?._id!!)
     }else{
      getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
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


 fun openLikedUserList(){
  getNavigator()?.openActivity(LikedPostUserListActivity::class.java, Bundle().apply {
   putString(Constants.POST_ID, data?._id)
  })
 }

 fun shareOnWhatsApp(){
getNavigator()?.sharePost(data?.linkUrl!!)
 }



}