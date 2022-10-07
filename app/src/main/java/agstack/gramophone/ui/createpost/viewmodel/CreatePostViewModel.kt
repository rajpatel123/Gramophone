package agstack.gramophone.ui.createpost.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.ui.createpost.CreatePostNavigator
import agstack.gramophone.utils.Constants
import android.view.View
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
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


}