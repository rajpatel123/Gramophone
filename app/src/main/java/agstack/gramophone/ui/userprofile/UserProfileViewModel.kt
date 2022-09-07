package agstack.gramophone.ui.userprofile

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.address.view.AddOrUpdateAddressActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.profile.model.ProfileResponse
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
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<UserProfileNavigator>() {
    var isFarmerSelected = ObservableField<Boolean>(true)
    var isTraderSelected = ObservableField<Boolean>(true)
    var profileImage = MutableLiveData<String>()
    private var userProfileJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)
    var userProfileData = ObservableField<GpApiResponseProfileData>()

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
            putParcelable(Constants.USER_PROFILE_DATA,userProfileData.get())
           //Add user Details
        })
    }

    fun onAddFirmClick(){
        getNavigator()?.openActivity(AddFirmActivity::class.java)
    }

    fun getUsersData() {
        userProfileJob.cancelIfActive()
        userProfileJob = checkNetworkThenRun {
            progressLoader.set(true)

            val userProfileResponse =
                onBoardingRepository.getProfile()

            if (userProfileResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                progressLoader.set(false)
                val userProfileResponseData: GpApiResponseProfileData? = userProfileResponse.body()?.gp_api_response_data
                userProfileData.set(userProfileResponseData)


                getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
            } else {
                progressLoader.set(false)
                getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
            }
        }
    }


    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    fun onFarmerLayoutSelected(){
        isFarmerSelected.set(!(isFarmerSelected.get()!!))
    }

    fun onTraderLayoutSelected(){
        isTraderSelected.set(!(isTraderSelected.get()!!))
    }
}