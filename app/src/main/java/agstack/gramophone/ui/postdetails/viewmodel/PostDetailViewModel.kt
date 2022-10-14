package agstack.gramophone.ui.postdetails.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.model.Data
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.FileUploadRequestBody
import agstack.gramophone.utils.Utility
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
 var commentInput = ObservableField<String>()

 var postDesc = ObservableField<String>()
 var likeCount = ObservableField<String>()
 var commentCount = ObservableField<String>()
 var imageAvailable = ObservableField<Boolean>()

 var postImage = ObservableField<File>()
 var isLoading = ObservableField<Boolean>()
 lateinit var tags: List<Map<String, String>>
 lateinit var postId: String
 fun getPostDetails(postId: String) {
  viewModelScope.launch {
   getNavigator()?.onLoading()
   tags = ArrayList()

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

      if (data?.tags!=null && data?.tags!!.size>0){
       getNavigator()?.setTags(data?.tags!!)
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
    val response = communityRepository.getPostComments(GetCommentRequestModel(postId, 100))
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


 fun openLikedUserList() {
  getNavigator()?.openActivity(LikedPostUserListActivity::class.java, Bundle().apply {
   putString(Constants.POST_ID, data?._id)
  })
 }

 fun shareOnWhatsApp() {
  getNavigator()?.sharePost(data?.linkUrl!!)
 }

 fun onCloseClick() {
  getNavigator()?.finishActivity()
 }

 fun sendComment() {

  if (TextUtils.isEmpty(commentInput.get())) {
   getNavigator()?.showToast(getNavigator()?.getMessage(R.string.enter_description))
   return
  }
  viewModelScope.launch {
   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     isLoading.set(true)

     val postID: RequestBody = data?._id?.toRequestBody("text/plain".toMediaTypeOrNull())!!
     val text: RequestBody = commentInput.get()!!.toRequestBody("text/plain".toMediaTypeOrNull())
     val tags: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())

     if (postImage.get() != null) {
      val imageUpoadRequestBody = FileUploadRequestBody(postImage.get()!!)
      val content: MultipartBody.Part = MultipartBody.Part.createFormData(
       "image",
       postImage.get()!!.name,
       imageUpoadRequestBody
      )

      val response = communityRepository.postComment(postID, text, tags, content)
      if (response.isSuccessful) {
       isLoading.set(false)

       commentInput.set("")
       getPostComments(postId = data?._id!!)
       getNavigator()?.clearImage()
      } else {
       isLoading.set(false)

       getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
      }
     } else {
      val response = communityRepository.postComment(postID, text, tags)
      if (response.isSuccessful) {
       isLoading.set(false)

       commentInput.set("")
       getPostComments(postId = data?._id!!)
      } else {
       isLoading.set(false)

       getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
      }
     }

    } else
     getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
   } catch (ex: Exception) {
    isLoading.set(false)

    when (ex) {
     is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
     else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
    }
   }

  }
 }

 fun captureImage() {
  var hasCameraPermission = getNavigator()?.requestPermission(Constants.CAMERA_PERMISSION)
  if (hasCameraPermission!!) {
   getNavigator()?.openCameraToCapture()
  }
 }

 fun deleteImage() {
  getNavigator()?.clearImage()
 }

}