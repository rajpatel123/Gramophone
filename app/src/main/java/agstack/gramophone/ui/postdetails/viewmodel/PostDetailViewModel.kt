package agstack.gramophone.ui.postdetails.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.model.LastComment
import agstack.gramophone.utils.Utility
import android.view.View
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

 var authorName = ObservableField<String>()
 var authorLocation = ObservableField<String>()
 var postDate = ObservableField<String>()
 var postDesc = ObservableField<String>()
 var likeCount = ObservableField<Int>()
 var commentCount = ObservableField<Int>()
 var imageAvailable = ObservableField<Boolean>()
 fun getPostDetails(postId:String) {

 viewModelScope.launch {
  getNavigator()?.onLoading()
  try {
   if (getNavigator()?.isNetworkAvailable() == true) {
    val response = communityRepository.getPostDetails(postId)
    if (response.isSuccessful) {
     val data = response.body()?.data
     authorName.set(data?.author?.username)
    // authorLocation.set(data?.author)
     postDesc.set(""+data?.description)
     likeCount.set(data?.likesCount)
     commentCount.set(data?.commentsCount)
     if (data?.liked == true) {
      getNavigator()?.setLikeImage(R.drawable.ic_liked)
     } else {
      getNavigator()?.setLikeImage(R.drawable.ic_like)
     }
     if (data?.images?.size!! >0) {
      getNavigator()?.onImageSet(data?.images[0].url)
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

}