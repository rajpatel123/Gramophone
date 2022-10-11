package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.createnewpost.view.model.MentionRequestModel
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.ui.tagandmention.Tag
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<CreatePostNavigator>() {
    var imageNo: Int =Constants.IV_ONE
    var listOfImages: HashMap<String, File> = hashMapOf()

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

}