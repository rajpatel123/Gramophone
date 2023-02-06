package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.profile.model.ValidateOtpMobileRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.RESEND_OTP_TIME
import agstack.gramophone.utils.Utility
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class VerifyOtpDialogViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<VerifyOtpDialogNavigator>() {
    var otp = ObservableField<String>()
    private var sendOTPJob: Job? = null
    private var reSendOTPJob: Job? = null
    private var verifyOTPJob: Job? = null
    var resendOTPType = ObservableField<String>()
    var remainigTimeForOTP = ObservableField<String>()
    var mobileNo = ObservableField<String>()
    var timeOver = ObservableField<Boolean>(false)
    var isVerifyEnable = ObservableField<Boolean>(true)
    var progressLoader = ObservableField<Boolean>(false)
    var type: String = Constants.SMS
    var remaningDuration: Long = 0
    var otp_reference_id: Int? = null


    fun showInitialValues(mobileNoBundle: String,OTPReferenceIDBundle :Int){
        otp_reference_id=OTPReferenceIDBundle
        mobileNo.set(mobileNoBundle)
        val text: String =
            java.lang.String.format(getNavigator()?.getMessage(R.string.resend_otp)!!, type)
        resendOTPType.set(text)
        getNavigator()?.showTimer(Constants.RESEND_OTP_TIME)




    }


    fun onResendClicked(v: View) {
        when (v.id) {
            R.id.tvSMS -> {
                resendOTP(Constants.SMS)
            }

            R.id.tvCall -> {
                resendOTP(Constants.VOICE)

            }
        }

    }

    private fun resendOTP(type: String) {
        reSendOTPJob.cancelIfActive()
        reSendOTPJob = checkNetworkThenRun {

            progressLoader.set(true)
            var resendOtpRequestModel = SendOtpRequestModel()
            resendOtpRequestModel.otp_reference_id = otp_reference_id
            resendOtpRequestModel.retryType = type

            getNavigator()?.sendResendOtpMoEngageEvent()
            val reSendOTPResponse =
                onBoardingRepository.resendOTPMobile(resendOtpRequestModel)

            val body = reSendOTPResponse.body()

            if (body?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                progressLoader.set(false)
                getNavigator()?.showToast(body?.gp_api_message)
                val text: String =
                    java.lang.String.format(
                        getNavigator()?.getMessage(R.string.resend_otp)!!,
                        type.uppercase()
                    )
                resendOTPType.set(text)
               getNavigator()?.showTimer(RESEND_OTP_TIME)

            } else {
                progressLoader.set(false)
                getNavigator()?.showToast(body?.gp_api_message)

            }

        }
    }

    fun onVerifyClick() {
        //If OTP is! null and OTP_reference_id!=null
        verifyOTPJob.cancelIfActive()
        try {
            verifyOTPJob = checkNetworkThenRun {

                progressLoader.set(true)
                if (otp.get().isNullOrEmpty()) {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_otp)!!)
                } else if (otp.get()?.length!! < 6) {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_6_digit_otp)!!)
                } else {
                    val validateOtpRequestModel =
                        ValidateOtpMobileRequestModel(otp.get()!!, otp_reference_id.toString())
                    val validateOTPResponse =
                        onBoardingRepository.validateOtpMobile(validateOtpRequestModel)

                    val body = validateOTPResponse.body()

                    if (body != null && body?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                        progressLoader.set(false)
                        getNavigator()?.showToast(body?.gp_api_message)
                        getNavigator()?.dismissDialogFragment(Constants.GP_API_STATUS)
                        getNavigator()?.sendIsOtpVerifiedMoEngageEvent(true)
                    } else {
                        progressLoader.set(false)
                        getNavigator()?.showToast(Utility.getErrorMessage(validateOTPResponse.errorBody()))
                        getNavigator()?.sendIsOtpVerifiedMoEngageEvent(false)
                    }
                }


            }

        } catch (e: Exception) {
            getNavigator()?.sendIsOtpVerifiedMoEngageEvent(false)

        }
    }

    fun onCancelClick() {
        getNavigator()?.dismissDialogFragment(null)

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

    fun calculateRemainigTime(millis: Long) {
        val ms = java.lang.String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    millis
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
        )
        remainigTimeForOTP.set(ms + getNavigator()?.getMessage(R.string.seconds))
    }

}