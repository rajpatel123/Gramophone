package agstack.gramophone.ui.userprofile.firm

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFirmViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<AddFirmNavigator>() {
    private var updateProfileJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)

    var firmName = ObservableField<String>()


    fun SaveandContinue() {
        updateProfileJob.cancelIfActive()
        updateProfileJob = checkNetworkThenRun {
            progressLoader.set(true)
            var updateProfileModel = UpdateProfileModel()
            updateProfileModel.firm_name = firmName.get()
            val userProfileResponse =
                onBoardingRepository.updateProfile(updateProfileModel)
            progressLoader.set(false)
            if (userProfileResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {


                getNavigator()?.showToast(userProfileResponse.body()?.gp_api_message)
            } else {

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

    fun setUserData(userData: GpApiResponseProfileData) {
        firmName.set(userData.firm_name)
    }

}