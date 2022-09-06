package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.view.AddOrUpdateAddressActivity
import agstack.gramophone.ui.userprofile.firm.AddFirmActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.CAMERA_PERMISSION
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.widget.FilePicker
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<UserProfileNavigator>() {
    var pictureFilePath: String? = ""
    var isFarmerSelected = ObservableField<Boolean>(true)
    var isTraderSelected = ObservableField<Boolean>(true)
    var profileImage = MutableLiveData<String>()

    fun profileImageSelect() {

        var hasCameraPermission = getNavigator()?.requestPermission(CAMERA_PERMISSION)
        if (hasCameraPermission!!) {
            getNavigator()?.openCameraToCapture()
        }

    }

    fun setProfilePic() {
        profileImage.value = SharedPreferencesHelper.instance?.getString(
            SharedPreferencesKeys.USER_IMAGE
        )
    }

    fun onEditAddressClick() {
        getNavigator()?.openActivity(AddOrUpdateAddressActivity::class.java, Bundle().apply {
            putBoolean(Constants.FROM_EDIT_PROFILE, true)
        })
    }

    fun onEditProfileClick(){
        getNavigator()?.openActivity(EditProfileActivity::class.java, Bundle().apply {
           //Add user Details
        })
    }

    fun onAddFirmClick(){
        getNavigator()?.openActivity(AddFirmActivity::class.java)
    }
}