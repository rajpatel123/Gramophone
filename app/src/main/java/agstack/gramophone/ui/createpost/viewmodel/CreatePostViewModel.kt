package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.createnewpost.model.MentionRequestModel
import agstack.gramophone.ui.createnewpost.view.model.create.CreatePostResponseModel
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.home.view.fragments.market.model.CropResponse
import agstack.gramophone.ui.postdetails.model.Image
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.POST_ID
import agstack.gramophone.utils.FileUploadRequestBody
import agstack.gramophone.utils.Utility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
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
    private var creatingPost: Boolean= false
    private lateinit var postId: String
    private lateinit var response: retrofit2.Response<CreatePostResponseModel>
    lateinit var imageUrls: List<Image>
    lateinit var cropResponse: CropResponse
    var imageNo: Int = Constants.IV_ONE
    var listOfImages: HashMap<String, File> = hashMapOf()
    var description = ObservableField<String>()
    var tags = ArrayList<JSONObject>()
    var removedImages = ArrayList<JSONObject>()
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
                if (listOfImages.containsKey("1")) {
                    listOfImages.remove("1")
                } else {
                    removedImages.add(JSONObject().apply {
                        put("id", imageUrls[0].id)
                    })
                }
            }

            R.id.ivDeleteSmall1 -> {
                imageNo = Constants.IV_TWO
                if (listOfImages.containsKey("2")) {
                    listOfImages.remove("2")
                } else {
                    removedImages.add(JSONObject().apply {
                        put("id", imageUrls[1].id)
                    })
                }
            }

            R.id.ivDeleteSmall2 -> {
                imageNo = Constants.IV_THREE
                if (listOfImages.containsKey("3")) {
                    listOfImages.remove("3")
                } else {
                    removedImages.add(JSONObject().apply {
                        put("id", imageUrls[2].id)
                    })
                }

            }
        }
        getNavigator()?.updateAddDeleteBtn(imageNo)
    }

    fun createPost() {


        if (!creatingPost){
            creatingPost = true
            viewModelScope.launch {
                if (getNavigator()?.getText().isNotNull() && getNavigator()?.getText()?.length!! >=20 ){
                    try {
                        if (getNavigator()?.isNetworkAvailable() == true) {
                            val desc: RequestBody =
                                getNavigator()?.getText()?.toRequestBody("text/plain".toMediaTypeOrNull()) ?: "".toRequestBody("text/plain".toMediaTypeOrNull())
                            val tags: RequestBody = tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                            var image1: MultipartBody.Part? =null
                            var image2:MultipartBody.Part? =null
                            var image3: MultipartBody.Part? =null
                            if (!listOfImages.isEmpty()){
                                val keys = listOfImages.keys
                                if (keys.size>0){
                                    keys.forEach {
                                        when (it) {
                                            "1" -> {
                                                val imageUpoadRequestBody = FileUploadRequestBody(
                                                    listOfImages[it]!!
                                                )
                                                Log.d("Image", "" + listOfImages[it]?.path)
                                                image1 = MultipartBody.Part.createFormData(
                                                    "image",
                                                    listOfImages.get(it)!!.name,
                                                    imageUpoadRequestBody
                                                )
                                            }
                                            "2" -> {
                                                val imageUpoadRequestBody = FileUploadRequestBody(
                                                    listOfImages[it]!!
                                                )
                                                Log.d("Image", "" + listOfImages[it]?.path)

                                                image2 = MultipartBody.Part.createFormData(
                                                    "image",
                                                    listOfImages.get(it)!!.name,
                                                    imageUpoadRequestBody
                                                )
                                            }
                                            "3" -> {
                                                val imageUpoadRequestBody = FileUploadRequestBody(
                                                    listOfImages[it]!!
                                                )
                                                Log.d("Image", "" + listOfImages[it]?.path)

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
                            response = communityRepository.createPost(desc, tags, image1, image2, image3)
                            if (response.isSuccessful) {
                                creatingPost = false
                                getNavigator()?.openAndFinishActivity(PostDetailsActivity::class.java,
                                    Bundle().apply {
                                        putString(POST_ID, response.body()?.data?._id)
                                    }
                                )
                            } else {
                                getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                            }

                        } else
                            getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                    } catch (ex: Exception) {
                        creatingPost = false

                        when (ex) {
                            is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                            else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                        }
                    }
                }else{
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.enter_desc_validation)!!)
                }

            }

        }


    }


    fun updatePost() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val desc: RequestBody =
                        description.get()?.toRequestBody("text/plain".toMediaTypeOrNull())
                            ?: "".toRequestBody("text/plain".toMediaTypeOrNull())
                    val tags: RequestBody =
                        tags.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    var image1: MultipartBody.Part? = null
                    var image2: MultipartBody.Part? = null
                    var image3: MultipartBody.Part? = null
                    if (!listOfImages.isEmpty()) {
                        val keys = listOfImages.keys
                        if (keys.size > 0) {
                            keys.forEach {
                                when (it) {
                                    "1" -> {
                                        val imageUpoadRequestBody = FileUploadRequestBody(
                                            listOfImages[it]!!
                                        )
                                        Log.d("Image", "" + listOfImages[it]?.path)
                                        image1 = MultipartBody.Part.createFormData(
                                            "image",
                                            listOfImages.get(it)!!.name,
                                            imageUpoadRequestBody
                                        )
                                    }
                                    "2" -> {
                                        val imageUpoadRequestBody = FileUploadRequestBody(
                                            listOfImages[it]!!
                                        )
                                        Log.d("Image", "" + listOfImages[it]?.path)

                                        image2 = MultipartBody.Part.createFormData(
                                            "image",
                                            listOfImages.get(it)!!.name,
                                            imageUpoadRequestBody
                                        )
                                    }
                                    "3" -> {
                                        val imageUpoadRequestBody = FileUploadRequestBody(
                                            listOfImages[it]!!
                                        )
                                        Log.d("Image", "" + listOfImages[it]?.path)

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

                    val removeImage: RequestBody =
                        removedImages.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val postID: RequestBody =
                        postId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    response = communityRepository.updatePost(
                        desc,
                        tags,
                        removeImage,
                        postID,
                        image1,
                        image2,
                        image3
                    )

                    if (response.isSuccessful) {
                        getNavigator()?.openAndFinishActivity(
                            PostDetailsActivity::class.java,
                            Bundle().apply {
                                putString(POST_ID, response.body()?.data?._id)
                            }
                        )
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

    fun getSearchSuggestion(text: String?) {
        viewModelScope.launch {
            if (text != null && text.length > 0) {
                val response = onBoardingRepository.getHasTags(MentionRequestModel(text))
                if (response.isSuccessful) {
                    val a = response.body()?.gp_api_response_data
                    val tags = arrayListOf<Tag>().apply {
                        for (n in 0..(a?.size!!) - 1) {
                            add(Tag("", '#', a?.get(n)?.tag, "", "", ""))
                        }
                    }
                    val tagArray: Array<Tag> = tags.toTypedArray()
                    getNavigator()?.populateHasTagList(tagArray)
                }
            }
        }
    }

    fun getProblems(text: String?) {
        viewModelScope.launch {
            if (text != null && text.length > 0) {
                val response = onBoardingRepository.getProblemTags(MentionRequestModel(text))
                if (response.isSuccessful) {
                    val a = response.body()?.gp_api_response_data
                    val tags = arrayListOf<Tag>().apply {
                        for (n in 0..(a?.size!!) - 1) {
                            add(Tag("", '$', a?.get(n)?.name, "", "", ""))
                        }
                    }
                    val tagArray: Array<Tag> = tags.toTypedArray()
                    getNavigator()?.populateProblemList(tagArray)
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
                            add(Tag("", '@', a?.get(n)?.tag, "", a?.get(n)?.uuid, a?.get(n)?.handle))
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
        this.postId = postId
        viewModelScope.launch {
            getNavigator()?.onLoading()
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.getPostDetails(postId)
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        description.set(data?.description.toString())

                        if (data?.images != null && data.images.size > 0) {
                            getNavigator()?.loadImages(data.images)
                        }
                        if (data?.tags != null && data?.tags.size > 0) {
                            getNavigator()?.showTags(arrayListOf<CropData>().apply {
                                for (n in 0..(data?.tags?.size!!) - 1) {
                                    add(CropData(null, data?.tags[n].tag, null, false, "", ""))
                                }

                            })
                            data?.tags.forEach { tag ->
                                if (cropResponse.gpApiResponseData?.cropsList?.size!! > 0) {
                                    cropResponse.gpApiResponseData?.cropsList?.forEach { cropData ->
                                        if (tag.tag.equals(cropData.cropName)) {
                                            cropData.isSelected = true
                                        }
                                    }
                                }
                            }

                        }


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
                    getNavigator()?.getPostDetails()
                   this@CreatePostViewModel.cropResponse = response.body()!!
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }
            }
        }
    }

}