package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.utils.Constants.CAMERA_PERMISSION
import agstack.gramophone.widget.FilePicker
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<UserProfileNavigator>() {
    var pictureFilePath: String? = ""
    fun profileImageSelect(){
        println("Helooooo")
        var hasCameraPermission = getNavigator()?.requestPermission(CAMERA_PERMISSION)
        if(hasCameraPermission!!){
            getNavigator()?.openCameraToCapture()
        }

       /* getNavigator()?.showImageSelect(FilePicker(false), {

            var hasCameraPermission = getNavigator()?.requestPermission(CAMERA_PERMISSION)
            if(hasCameraPermission!!){
                getNavigator()?.openCameraToCapture()
            }

        },{

        })*/
    }
}