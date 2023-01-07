package agstack.gramophone.ui.userprofile.firm

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddFirmViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<AddFirmNavigator>() {
    lateinit var UserProfileData :GpApiResponseProfileData
    private var updateProfileJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)

    var firmName = ObservableField<String>()


    fun SaveandContinue() {
        updateProfileJob.cancelIfActive()
        updateProfileJob = checkNetworkThenRun {
            progressLoader.set(true)
            try {
            var updateProfileModel = UpdateProfileModel()
            updateProfileModel.is_trader = UserProfileData.is_trader
            updateProfileModel.is_farmer=UserProfileData.is_farmer
            updateProfileModel.firm_name = firmName.get()
            val userProfileResponse =
                onBoardingRepository.updateProfile(updateProfileModel)
            progressLoader.set(false)
            if (userProfileResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                getNavigator()?.sendSaveFirmMoEngageEvent(firmName.get()!!)
                getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
                getNavigator()?.finishActivity()
            } else {

                getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
            }
        }catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
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

    fun setUserData(userData: GpApiResponseProfileData) {
        UserProfileData= userData
        firmName.set(userData.firm_name)
    }

}