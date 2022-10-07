package agstack.gramophone.ui.comments.viewModel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.comments.CommentNavigator
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.FileUploadRequestBody
import agstack.gramophone.utils.Utility
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
    var commentsCount = ObservableField<String>()
    var commentInput = ObservableField<String>()
    var postImage = ObservableField<File>()
    var isLoading = ObservableField<Boolean>()
    lateinit var tags:List<Map<String,String>>
    lateinit var postId: String
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
                        getNavigator()?.updateCommentsList(commentsAdapter){

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

    fun onCloseClick(){
        getNavigator()?.finishActivity()
    }

    fun sendComment(){

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