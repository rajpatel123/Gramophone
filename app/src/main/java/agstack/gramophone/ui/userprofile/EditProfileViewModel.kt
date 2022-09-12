package agstack.gramophone.ui.userprofile

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.userprofile.verifyotp.model.VerifyOTPRequestModel
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<EditProfileNavigator>() {

    var firstName = ObservableField<String>()
    var lastName = ObservableField<String>()
    var mobileNo = ObservableField<String>()
    private var sendOTPJob: Job? = null
    var OTP = ObservableField<String>()
    var userDatafromIntent = ObservableField<GpApiResponseProfileData>()
    var otp_reference_id: Int? = null
    var progressLoader = ObservableField<Boolean>(false)

    fun updateProfile() {


    }

    fun sendOTP(mobileNo: String) {
        val sendOTPRequestModel = VerifyOTPRequestModel()

        sendOTPRequestModel.mobile_no = mobileNo

        sendOTPJob.cancelIfActive()
        sendOTPJob = checkNetworkThenRun {
            progressLoader.set(true)

            val sendOTPResponse =
                onBoardingRepository.sendOTPMobile(sendOTPRequestModel)

            val body = sendOTPResponse.body()

            if (body?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                progressLoader.set(false)
                getNavigator()?.showToast(body?.gp_api_message)
                otp_reference_id = body?.gp_api_response_data?.otp_reference_id

                getNavigator()?.showVerifyOTPFragment(otp_reference_id!!)


            } else {
                progressLoader.set(false)
                getNavigator()?.showToast(body?.gp_api_message)

            }
        }

    }

    fun onUpdateClick() {
        if (userDatafromIntent.get()?.mobile_no.equals(mobileNo.get())) {
            getNavigator()?.showToast(R.string.enter_different_mobile_no)
        } else {

            sendOTP(mobileNo.get()!!)
        }
    }

    fun setUserData(userData: GpApiResponseProfileData) {
        userDatafromIntent.set(userData)
        firstName?.set(userDatafromIntent.get()?.first_name)
        lastName?.set(userDatafromIntent.get()?.last_name)
        mobileNo?.set(userDatafromIntent.get()?.mobile_no)
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


}