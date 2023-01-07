package agstack.gramophone.ui.comments.viewModel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.comments.CommentNavigator
import agstack.gramophone.ui.comments.model.Data
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.utils.*
import android.os.SystemClock
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.moengage.core.Properties
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
class CommentsViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
):BaseViewModel<CommentNavigator> (){
    var mLastClickTime: Long = 0
    var commentsCount = ObservableField<String>()
    var commentInput = ObservableField<String>()
    var postImage = ObservableField<File>()
    var isLoading = ObservableField<Boolean>()
    var showImageFullScreen = ObservableField<Boolean>()
    lateinit var tags:List<Map<String,String>>
    lateinit var postId: String
    var comment:Data? = null
    fun getComments(postId: String) {
        this.postId = postId
        tags = ArrayList()
        commentInput.set("")
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.getPostComments(GetCommentRequestModel(postId,1000))
                    if (response.isSuccessful) {
                        val data = response.body()?.data

                        commentsCount.set(getNavigator()?.getMessage(R.string.comments)
                            ?.let { String.format(it,data?.size) })
                          var commentsAdapter = CommentsAdapter(data)
                        getNavigator()?.updateCommentsList(commentsAdapter,
                            {
                              deleteComment(it)
                            },
                            {
                                comment = it
                             getNavigator()?.populateCommentData(it)
                            },{
                                showImageFullScreen.set(true)
                                getNavigator()?.showImage(it)
                            })
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

    fun onImageClose(){
        showImageFullScreen.set(false)
        //getNavigator()?.finishActivity()
    }
    fun onCloseClick(){
        getNavigator()?.finishActivity()
    }

    fun sendComment(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (comment!=null){
            updateComment()
            return
        }
        if (TextUtils.isEmpty(commentInput.get())){
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.enter_description))
            return
        }
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    isLoading.set(true)

                    val postID: RequestBody = postId.toRequestBody("text/plain".toMediaTypeOrNull())
                    val text: RequestBody = commentInput.get()!!.toRequestBody("text/plain".toMediaTypeOrNull())
                    val tags: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    if (postImage.get()!=null){
                        val imageUpoadRequestBody = FileUploadRequestBody(postImage.get()!!)
                        val content: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "image",
                            postImage.get()!!.name,
                            imageUpoadRequestBody
                        )

                        val response = communityRepository.postComment(postID,text,tags,content)
                        if (response.isSuccessful) {
                            isLoading.set(false)

                            commentInput.set("")
                            getComments(postId = postId)
                            getNavigator()?.clearImage()
                        }else{
                            isLoading.set(false)

                            getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                        }

                    }else{
                        val response = communityRepository.postComment(postID,text,tags)
                        if (response.isSuccessful) {
                            isLoading.set(false)

                            commentInput.set("")
                            getComments(postId = postId)


                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!)
                                .addAttribute("Post_ID",postID)
                                .addAttribute("Comment ID",response.body()?.data?._id)
                                .addAttribute("Comment",comment)
                                .setNonInteractive()
                            getNavigator()?.sendMoEngageEvent("KA_Write_Comment", properties)


                        }else{
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
    fun updateComment(){

        if (TextUtils.isEmpty(commentInput.get())){
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.enter_description))
            return
        }
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    isLoading.set(true)

                    val postID: RequestBody = postId.toRequestBody("text/plain".toMediaTypeOrNull())
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
                            getComments(postId = postId)
                            getNavigator()?.clearImage()

                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!)
                                .addAttribute("Post_ID",postId)
                                .addAttribute("Comment ID",id)
                                .setNonInteractive()
                            getNavigator()?.sendMoEngageEvent("KA_Edit_Comment", properties)

                        }else{
                            isLoading.set(false)

                            getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                        }
                    }else{
                        val response = communityRepository.updateComment(postID,id,text,tags)
                        if (response.isSuccessful) {
                            isLoading.set(false)

                            commentInput.set("")
                            getComments(postId = postId)
                            val properties = Properties()
                            properties.addAttribute(
                                "Customer_Id",
                                SharedPreferencesHelper.instance?.getString(
                                    SharedPreferencesKeys.CUSTOMER_ID
                                )!!)
                                .addAttribute("Post_ID",postId)
                                .addAttribute("Comment ID",id)
                                .setNonInteractive()
                            getNavigator()?.sendMoEngageEvent("KA_Edit_Comment", properties)
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


    fun deleteComment(data: Data) {
    if (getNavigator()?.isNetworkAvailable()==true){
        viewModelScope.launch {
            val deleteCommentResponse = communityRepository.deleteComment(data.postId,data._id)
            if (deleteCommentResponse.isSuccessful && deleteCommentResponse.body()?.data == true){
                getComments(data.postId)

                val properties = Properties()
                properties.addAttribute(
                    "Customer_Id",
                    SharedPreferencesHelper.instance?.getString(
                        SharedPreferencesKeys.CUSTOMER_ID
                    )!!)
                    .addAttribute("Post_ID",postId)
                    .addAttribute("Comment ID",data?._id)
                    .setNonInteractive()
                getNavigator()?.sendMoEngageEvent("KA_Delete_Comment", properties)
            }else{
                getNavigator()?.showToast(Utility.getErrorMessage(deleteCommentResponse.errorBody()))
            }
        }
    }
    }

    fun captureImage(){
        var hasCameraPermission = getNavigator()?.requestPermission(Constants.CAMERA_PERMISSION)
        if (hasCameraPermission!!) {
            getNavigator()?.openCameraToCapture()
        }
    }

    fun deleteImage(){
        getNavigator()?.clearImage()
    }
}