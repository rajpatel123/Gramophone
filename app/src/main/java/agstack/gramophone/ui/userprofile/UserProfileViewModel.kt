package agstack.gramophone.ui.userprofile

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.view.AddOrUpdateAddressActivity
import agstack.gramophone.ui.followings.view.FollowerFollowedActivity
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.firm.AddFirmActivity
import agstack.gramophone.ui.userprofile.model.TestUserModel
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.CAMERA_PERMISSION
import agstack.gramophone.utils.Constants.PAGE
import agstack.gramophone.utils.Constants.PAGE_SOURCE
import agstack.gramophone.utils.FileUploadRequestBody
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
    private val communityRepository: CommunityRepository
) : BaseViewModel<UserProfileNavigator>() {
    var isFarmerSelected = ObservableField<Boolean>(false)
    var isTraderSelected = ObservableField<Boolean>(false)
    var profileImage = MutableLiveData<String>()
    private var userProfileJob: Job? = null
    private var updateProfileJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)
    var userProfileData = ObservableField<GpApiResponseProfileData>()
    var userHandle=ObservableField<String>()

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
            putParcelable(Constants.ADDRESSOBJECT ,userProfileData.get()?.address_data )
        })
    }

    fun onEditProfileClick() {
        getNavigator()?.openActivity(EditProfileActivity::class.java, Bundle().apply {
            putParcelable(Constants.USER_PROFILE_DATA, userProfileData.get())
            //Add user Details
        })
    }

    fun onAddFirmClick() {
        getNavigator()?.openActivity(AddFirmActivity::class.java, Bundle().apply {
            putParcelable(Constants.USER_PROFILE_DATA, userProfileData.get())
            //Add user Details
        })
    }

    fun getUsersData() {
        userProfileJob.cancelIfActive()
        userProfileJob = checkNetworkThenRun {
            progressLoader.set(true)

            val userProfileResponse =
                onBoardingRepository.getProfile()

            if (userProfileResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                progressLoader.set(false)
                val userProfileResponseData: GpApiResponseProfileData? =
                    userProfileResponse.body()?.gp_api_response_data
                userProfileData.set(userProfileResponseData)
                isFarmerSelected.set(userProfileResponseData?.is_farmer)
                isTraderSelected.set(userProfileResponseData?.is_trader)


                //getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
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

    fun onFarmerLayoutSelected() {

        if (isFarmerSelected.get() == true) {
            if (isTraderSelected.get() == true) {
                isFarmerSelected.set(false)
                updateProfile(false)
            } else {
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_select_atleast_one_role))
            }

        } else {
            isFarmerSelected.set(true)
            updateProfile(true)
        }

    }

    fun onTraderLayoutSelected() {

        if (isTraderSelected.get() == true) {
            if (isFarmerSelected.get() == true) {
                isTraderSelected.set(false)
                updateProfile(null, false)
            } else {
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_select_atleast_one_role))
            }

        } else {
            isTraderSelected.set(true)
            updateProfile(null, true)
        }

    }

    fun updateProfile(
        is_farmer: Boolean? = null,
        is_trader: Boolean? = null,
        firm_name: String? = null,
        profileImage: File? = null
    ) {
        updateProfileJob.cancelIfActive()
        updateProfileJob = checkNetworkThenRun {
            progressLoader.set(true)
            if (profileImage != null) {

                profileImage.let {
                    val imageUpoadRequestBody = FileUploadRequestBody(profileImage)
                    val content: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "image",
                        profileImage.name,
                        imageUpoadRequestBody
                    )
                    val response: Response<TestUserModel> = communityRepository.updateUserProfileImage(content)
                    if(response.isSuccessful){
                        Log.d("Picture Posted","User Profile Updated")
                        userHandle.set(response.body()?.data?.handle)

                    }


                }
            } else {

                var updateProfileModel = UpdateProfileModel()
                updateProfileModel.firm_name = firm_name
                updateProfileModel.is_farmer = is_farmer
                updateProfileModel.is_trader = is_trader
                val userProfileResponse =
                    onBoardingRepository.updateProfile(updateProfileModel)
                progressLoader.set(false)
                if (userProfileResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                    // Refresh the page
                    getNavigator()?.refreshPage()



                    getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
                } else {
                    progressLoader.set(false)
                    getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
                }
            }
        }
    }

    fun onFollowerClicked(){
     getNavigator()?.openActivity(FollowerFollowedActivity::class.java, Bundle().apply {
         putString(PAGE, PAGE)
     })
    }

  fun  onFollowingClicked(){
      getNavigator()?.openActivity(FollowerFollowedActivity::class.java, Bundle().apply {
          putString(PAGE,"")
      })
  }
}