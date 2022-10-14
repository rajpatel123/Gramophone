package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.createnewpost.view.model.MentionRequestModel
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.POST_ID
import agstack.gramophone.utils.FileUploadRequestBody
import agstack.gramophone.utils.Utility
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val onBoardingRepository: OnBoardingRepository,
    private val productRepository: ProductRepository
) : BaseViewModel<CreatePostNavigator>() {
    lateinit var cropResponse: CropResponse
    var imageNo: Int =Constants.IV_ONE
    var listOfImages: HashMap<String, File> = hashMapOf()
    var description = ObservableField<String>()
    var tags = ArrayList<JSONObject>()
    fun finishActivity() {
        getNavigator()?.finishActivity()
    }

    fun onAddImageClicked(v: View) {

        when (v.id) {
            R.id.ivPlusSmall1 -> {
                imageNo = Constants.IV_TWO
            }

            R.id.ivPlusBig -> {
                imageNo = Constants.IV_ONE
            }

            R.id.ivPlusSmall2 -> {
                imageNo = Constants.IV_THREE
            }
        }


        var hasCameraPermission = getNavigator()?.requestPermission(Constants.CAMERA_PERMISSION)
        if (hasCameraPermission!!) {
            getNavigator()?.openCameraToCapture()
        }


    }
    fun onDeleteImage(v: View) {

        when (v.id) {
            R.id.ivDeletBig -> {
                imageNo = Constants.IV_ONE
                listOfImages.remove("1")
            }

            R.id.ivDeleteSmall1 -> {
                imageNo = Constants.IV_TWO
                listOfImages.remove("2")

            }

            R.id.ivDeleteSmall2 -> {
                imageNo = Constants.IV_THREE
                listOfImages.remove("3")
            }
        }
        getNavigator()?.updateAddDeleteBtn(imageNo)
    }

    fun createPost() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val desc: RequestBody =
                        description.get()?.toRequestBody("text/plain".toMediaTypeOrNull()) ?: "".toRequestBody("text/plain".toMediaTypeOrNull())
                        val tags: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    var image1: MultipartBody.Part? =null
                    var image2:MultipartBody.Part? =null
                    var image3: MultipartBody.Part? =null
                    if (!listOfImages.isEmpty()){
                      val keys = listOfImages.keys
                       if (keys.size>0){
                           if (keys.size==1){
                               keys.forEach {
                                   val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                   image1 = MultipartBody.Part.createFormData(
                                       "image",
                                       listOfImages.get(it)!!.name,
                                       imageUpoadRequestBody
                                   )

                               }

                           }else if (keys.size==2){
                               keys.forEach {
                                   when(it){
                                       "1"->{
                                           val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                           image1 = MultipartBody.Part.createFormData(
                                               "image",
                                               listOfImages.get(it)!!.name,
                                               imageUpoadRequestBody
                                           )
                                       }
                                       "2"->{
                                           val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                           image2 = MultipartBody.Part.createFormData(
                                               "image",
                                               listOfImages.get(it)!!.name,
                                               imageUpoadRequestBody
                                           )
                                       }

                                   }

                               }
                           }else if (keys.size==3){
                            keys.forEach {
                                when(it){
                                    "1"->{
                                        val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                        image1 = MultipartBody.Part.createFormData(
                                            "image",
                                            listOfImages.get(it)!!.name,
                                            imageUpoadRequestBody
                                        )
                                    }
                                    "2"->{
                                        val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                        image2 = MultipartBody.Part.createFormData(
                                            "image",
                                            listOfImages.get(it)!!.name,
                                            imageUpoadRequestBody
                                        )
                                    }
                                    "3"->{
                                        val imageUpoadRequestBody = FileUploadRequestBody(listOfImages.get(it)!!)
                                        image3 = MultipartBody.Part.createFormData(
                                            "image",
                                            listOfImages.get(it)!!.name,
                                            imageUpoadRequestBody
                                        )
                                    }
                                }
                            }
                           }
                       }


                    }
                    val response = communityRepository.createPost(desc,tags,image1,image2,image3)
                    if (response.isSuccessful) {
                        getNavigator()?.openAndFinishActivity(PostDetailsActivity::class.java,
                            Bundle().apply {
                                putString(POST_ID,response.body()?.data?._id)
                            }
                        )
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

    fun getSearchSuggestion(text: String?) {
        viewModelScope.launch {
            if (text != null && text.length > 0) {
                val response = onBoardingRepository.getHasTags(MentionRequestModel(text))
                if (response.isSuccessful) {
                    val a = response.body()?.gp_api_response_data
                    val tags =  arrayListOf<Tag>().apply {
                        for(n in 0..(a?.size!!)-1){
                            add(Tag("", null, a?.get(n)?.tag, "", "", ""))
                        }
                    }
                    val tagArray: Array<Tag> = tags.toTypedArray()
                    getNavigator()?.populateHasTagList(tagArray)
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }
            }
        }
    }

    fun getMentionSuggestion(text: String?) {
        viewModelScope.launch {
            if (text != null && text.length > 0) {
                val response = onBoardingRepository.getMentionTags(MentionRequestModel(text))
                if (response.isSuccessful) {
                    val a = response.body()?.gp_api_response_data
                    val tags =  arrayListOf<Tag>().apply {
                        for(n in 0..(a?.size!!)-1){
                            add(Tag("", null, a?.get(n)?.tag, "", a?.get(n)?.uuid, a?.get(n)?.handle))
                        }
                    }
                    val tagArray: Array<Tag> = tags.toTypedArray()
                   getNavigator()?.populateTagSuggestionList(tagArray)
                } else {
                   // getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }
            }
        }
    }


    fun getPostDetails(postId: String) {
        viewModelScope.launch {
            getNavigator()?.onLoading()
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.getPostDetails(postId)
                    if (response.isSuccessful) {

                    } else {
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

    fun getCrops() {
        tags = ArrayList()
        viewModelScope.launch {
            if (getNavigator()?.isNetworkAvailable() == true) {

                val response = productRepository.getCrops()
                if (response.isSuccessful) {
                   this@CreatePostViewModel.cropResponse = response.body()!!
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }
            }
        }
    }

}