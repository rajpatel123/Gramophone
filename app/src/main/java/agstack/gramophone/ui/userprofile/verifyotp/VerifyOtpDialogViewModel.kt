package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.utils.Constants
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyOtpDialogViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<VerifyOtpDialogNavigator>() {
    private var sendOTPJob: Job? = null
    var resendOTPType = ObservableField<String>()
    var remainigTimeForOTP = ObservableField<String>()
    var timeOver = ObservableField<Boolean>()
    var progressLoader = ObservableField<Boolean>(false)


    fun sendOTP(){
        val sendOtpRequestModel = SendOtpRequestModel()

        sendOtpRequestModel.phone = "9625147638"
        sendOtpRequestModel.language = getNavigator()?.getLanguage()

        //callback comes here when on add to cart is clicked
        Log.d("Click", "Add to cart Clicked")
        sendOTPJob.cancelIfActive()
        sendOTPJob = checkNetworkThenRun {
            progressLoader.set(true)

            val sendOTPResponse =
                onBoardingRepository.sendOTP(sendOtpRequestModel)

            if (sendOTPResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                progressLoader.set(false)
                getNavigator()?.showToast(sendOTPResponse.body()?.gp_api_message)
            } else {
                progressLoader.set(false)
                getNavigator()?.showToast(sendOTPResponse.body()?.gp_api_message)
            }
        }

    }



    fun onResendClicked(v: View) {
        when (v.id) {
            R.id.tvSMS -> {
               // resendOTP(Constants.SMS)
            }

            R.id.tvCall -> {
               // resendOTP(Constants.VOICE)

            }
        }

    }

    fun onVerifyClick(){
        Log.d("HHHH","")

    }

    fun onCancelClick(){

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