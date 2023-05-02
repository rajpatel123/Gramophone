package agstack.gramophone.ui.postdetails.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.home.view.fragments.community.LikedPostUserListActivity
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.ui.postdetails.PostDetailNavigator
import agstack.gramophone.ui.postdetails.model.Data
import agstack.gramophone.utils.*
import android.os.Build

import android.os.Bundle
import android.text.TextUtils
import androidx.core.text.HtmlCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.moengage.core.Properties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class PostDetailViewModel @Inject constructor(
 private val  communityRepository: CommunityRepository,
 private val productRepository: ProductRepository
) : BaseViewModel<PostDetailNavigator>() {

 private var data: Data? = null
 var authorName = ObservableField<String>()
 var authorLocation = ObservableField<String>()
 var isDataAvailable = ObservableField<Boolean>()
 var isAddress = ObservableField<Boolean>()
 var postDate = ObservableField<String>()
 var isAdmin = ObservableField<Boolean>(false)
 var commentInput = ObservableField<String>()

 var postDesc = ObservableField<String>()
 var likeCount = ObservableField<String>()
 var isLiked = ObservableField<Boolean>()
 var commentCount = ObservableField<String>()
 var imageAvailable = ObservableField<Boolean>()
 var isDate = ObservableField<Boolean>()
 var showingDate = ObservableField<String>()
 var showDateView = ObservableField<Boolean>()
 lateinit var cropResponse: CropResponse
 var comment: agstack.gramophone.ui.comments.model.Data? = null

 var postImage = ObservableField<File>()
 var isLoading = ObservableField<Boolean>()
 lateinit var postId: String
 var tags = ArrayList<JSONObject>()

 fun getPostDetails(postId: String) {
  viewModelScope.launch {
   getNavigator()?.onLoading()
   tags = ArrayList()

   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     val response = communityRepository.getPostDetails(postId)
     if (response.isSuccessful &&response.body()?.data!=null) {
      isDataAvailable.set(true)
      data = response.body()?.data
      authorName.set(data?.author?.username)
      // authorLocation.set(data?.author)
     if (data?.description != null){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
       getNavigator()?.setText(HtmlCompat.fromHtml(data?.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY))
      }else{
       getNavigator()?.setText(HtmlCompat.fromHtml(data?.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY))
      }
     }
      postDesc.set(data?.description.toString())

      postDate.set(data?.createdDate)

      if (!TextUtils.isEmpty(data?.author?.communityUserType) && "admin".equals(data?.author?.communityUserType,true)) {
      isAdmin.set(true)
      }

       if (data?.author?.address_short.isNotNullOrEmpty()){
       authorLocation.set(data?.author?.address_short)
       isAddress.set(true)
      }else{
       isAddress.set(false)
       authorLocation.set(getNavigator()?.getMessage(R.string.add_address))
      }

     likeCount.set(data?.likesCount.toString() +" "+getNavigator()?.getMessage(R.string.like))
     commentCount.set(data?.commentsCount.toString()+" "+getNavigator()?.getMessage(R.string.comment_count))
     if (data?.liked == true) {
      isLiked.set(true)
      getNavigator()?.setLikeImage(R.drawable.ic_liked)
     } else {
      isLiked.set(false)
      getNavigator()?.setLikeImage(R.drawable.ic_like)
     }

      if (data?.author?.uuid.equals(
        SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.UUIdKey)) ){
       showDateView.set(true)
       if (data?.showingDate==null){
        isDate.set(true)
       }else{
        isDate.set(false)
       }
      }else{
       showDateView.set(false)
      }

      if (data?.showingDate!=null){
       showingDate.set(""+data?.showingDate)
      }

      if (data?.farmArea!=null){

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
      getNavigator()?.onLoadingTextMessage()
     }
    }else{
      isDataAvailable.set(false)

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


  val properties = Properties()
  properties.addAttribute(
   "Customer_Id",
   SharedPreferencesHelper.instance?.getString(
    SharedPreferencesKeys.CUSTOMER_ID
   )!!)
   .addAttribute("Post_ID",postId)
   .setNonInteractive()
  getNavigator()?.sendMoEngageEvent("KA_View_Post_Detail", properties)
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
      getNavigator()?.updateCommentsList(commentsAdapter,
       {
        deleteComment(it)
      },{
       comment = it
       getNavigator()?.populateCommentData(it)
      })

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
  getNavigator()?.sharePost("https://www.gramophone.in/app?category=postDetails&postId="+data?._id)
 }

 fun onCloseClick() {
  getNavigator()?.finishActivity()
 }

 fun sendComment() {
  if (comment!=null){
   updateComment()
   return
  }
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

 fun getCrops() {
  tags = ArrayList()
  viewModelScope.launch {
   if (getNavigator()?.isNetworkAvailable() == true) {

    val response = productRepository.getCrops()
    if (response.isSuccessful) {
     this@PostDetailViewModel.cropResponse = response.body()!!
    } else {
     getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
    }
   }
  }
 }

 fun updatePost(area: JSONObject, date: String?, showingDate: Date) {
  viewModelScope.launch {
   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     val postID: RequestBody = data?._id!!.toRequestBody("text/plain".toMediaTypeOrNull())
     val tagss: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())
     val showDate: RequestBody = date.toString().toRequestBody("text/plain".toMediaTypeOrNull())
     val area: RequestBody = area.toString().toRequestBody("text/plain".toMediaTypeOrNull())

     val response = communityRepository.updatePost(postID,tagss,area,showDate)
     if (response.isSuccessful) {
      var hasImages = "No"
      if (response.body()?.data?.images.isNotNull() && response.body()?.data?.images?.size!! >0){
       hasImages ="Yes"
      }



      val properties = Properties()
      properties.addAttribute(
       "Customer_Id",
       SharedPreferencesHelper.instance?.getString(
        SharedPreferencesKeys.CUSTOMER_ID
       )!!)
       .addAttribute("Post_ID",data?._id!!)
       .addAttribute("Post_Text",response.body()?.data?.description)
       .addAttribute("Has_Images",hasImages)
       .addAttribute("Crop",tags.toString())
       .addAttribute("Area",area)
       .addAttribute("Sowing_Date",Utility.getShowingDate(showingDate))
       .setNonInteractive()
      getNavigator()?.sendMoEngageEvent("KA_Save_Post",properties)



      getPostDetails(data?._id!!)
     }else{
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

 fun showUpdatePostBottomSheet(){
 getNavigator()?.showBottomSheet()
 }

 fun updateComment(){

  if (TextUtils.isEmpty(commentInput.get())){
   getNavigator()?.showToast(getNavigator()?.getMessage(R.string.enter_description))
   return
  }
  viewModelScope.launch {
   try {
    if (getNavigator()?.isNetworkAvailable() == true) {
     isLoading.set(true)

     val postID: RequestBody = data?._id!!.toRequestBody("text/plain".toMediaTypeOrNull())
     val text: RequestBody = commentInput.get()!!.toRequestBody("text/plain".toMediaTypeOrNull())
     val tags: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())
     val id: RequestBody = comment?._id.toString().toRequestBody("text/plain".toMediaTypeOrNull())

     if (postImage.get()!=null){
      val imageUpoadRequestBody = FileUploadRequestBody(postImage.get()!!)
      val content: MultipartBody.Part = MultipartBody.Part.createFormData(
       "image",
       postImage.get()!!.name,
       imageUpoadRequestBody
      )

      val response = communityRepository.updateComment(postID,id,text,tags,content)
      if (response.isSuccessful) {
       isLoading.set(false)

       commentInput.set("")
       getPostComments(postId = data?._id!!)
       getNavigator()?.clearImage()
      }else{
       isLoading.set(false)

       getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
      }
     }else{
      val response = communityRepository.updateComment(postID,id,text,tags)
      if (response.isSuccessful) {
       isLoading.set(false)

       commentInput.set("")
       getPostComments(postId = data?._id!!)
      }else{
       isLoading.set(false)

       getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
      }
     }
     comment = null
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
 fun deleteComment(comment: agstack.gramophone.ui.comments.model.Data) {
  if (getNavigator()?.isNetworkAvailable()==true){
   viewModelScope.launch {
    val deleteCommentResponse = communityRepository.deleteComment(data?._id!!,comment._id)
    if (deleteCommentResponse.isSuccessful && deleteCommentResponse.body()?.data == true){
     getPostComments(data!!._id!!)
    }else{
     getNavigator()?.showToast(Utility.getErrorMessage(deleteCommentResponse.errorBody()))
    }
   }
  }
 }
}