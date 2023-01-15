package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.view.StateListActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.*
import agstack.gramophone.utils.Constants.REMAINING_TIME
import agstack.gramophone.utils.Constants.UNAUTHORIZED
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : BaseViewModel<VerifyOTPNavigator>() {
    var remaningDuration: Long = 0
    var otp = ObservableField<String>()
    var otpHint: String = ""
    var mobileNo = ObservableField<String>()
    var referralCode = ObservableField<String>()
    var otpReference = ObservableField<String>()
    var resendOTPType = ObservableField<String>()
    var remainigTimeForOTP = ObservableField<String>()
    var type: String = Constants.SMS


    var time: String = ""
    var timeOver = ObservableField<Boolean>()


    val validateOtpResponseModel: MutableLiveData<ApiResponse<ValidateOtpResponseModel>> =
        MutableLiveData()

    fun submitOtp() = viewModelScope.launch {
        if (otp.get().isNullOrEmpty()) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_otp)!!)
        } else if (otp.get()?.length!! < 6) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_6_digit_otp)!!)
        } else {
            val validateOtpRequestModel = ValidateOtpRequestModel(
                mobileNo.get().toString(), otp.get()!!,
                otpReference.get()?.toInt()!!,
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.UTM_SOURCE)
            )
            validateOtp(validateOtpRequestModel)
        }
    }

    private suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel) {

        validateOtpResponseModel.postValue(ApiResponse.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.validateOtp(validateOtpRequestModel)


                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (Constants.GP_API_STATUS.equals(responseData?.gp_api_status)) {
                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.session_token,
                            responseData?.gp_api_response_data?.token
                        )

                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.UUIdKey,
                            responseData?.gp_api_response_data?.uuid
                        )

                       getNavigator()?.setMoEngageUniqueID()

                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.IS_ADMIN,
                            responseData?.gp_api_response_data!!.community_user_type
                        )

                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.USER_PHONE_NUMBER,
                            mobileNo.get()
                        )

                        SharedPreferencesHelper.instance?.putBoolean(
                            SharedPreferencesKeys.WHATSAPP_OPT_IN,
                            responseData?.gp_api_response_data?.whatsapp_optin
                        )

                        if (responseData?.gp_api_response_data?.is_address == true) {
                            SharedPreferencesHelper.instance?.putBoolean(
                                SharedPreferencesKeys.logged_in,
                                true
                            )
                            SharedPreferencesHelper.instance?.putBoolean(
                                SharedPreferencesKeys.APP_TOUR_ENABLED,
                                true
                            )
                            SharedPreferencesHelper.instance?.putInteger(
                                SharedPreferencesKeys.APP_TOUR_SKIP_COUNT,
                                0
                            )
                            getNavigator()?.sendVerifiedOTPMoEngageEvent(mobileNo.get()!!, referralCode.get()!!)
                           // getNavigator()?.openAndFinishActivity(HomeActivity::class.java)
                            getNavigator()?.openAndFinishActivity(StateListActivity::class.java)

                        } else {
                            getNavigator()?.openAndFinishActivity(StateListActivity::class.java)
                        }
                        //getNavigator()?.showToast(responseData?.gp_api_message)
                    } else {
                        val arrayTutorialType =
                            object : TypeToken<ValidateOtpResponseModel>() {}.type
                        var errorResponse: ValidateOtpResponseModel =
                            Gson().fromJson(response.errorBody()?.string(), arrayTutorialType)
                        getNavigator()?.showToast(errorResponse.gp_api_message)

                    }
                } else if (response.code() == UNAUTHORIZED) {
                    getNavigator()?.getInitModel()?.let { refreshToken(it, isValidate = true) }
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }

            } else
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    private fun handleOrderResponse(response: Response<ValidateOtpResponseModel>): ApiResponse<ValidateOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun updateMessage() {
        if (!getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO).isNullOrEmpty()) {
            val bundle = getNavigator()?.getBundle()
            otpHint =
                getNavigator()?.getMessage(R.string.otp_hint)!!
            mobileNo.set(bundle?.getString(Constants.MOBILE_NO).toString())
            referralCode.set(bundle?.getString(Constants.REFERRAL_CODE).toString())
            otpReference.set(bundle?.getInt(Constants.OTP_REFERENCE).toString())
            if (!TextUtils.isEmpty(bundle?.getString(Constants.Otp)))
                otp.set(bundle?.getString(Constants.Otp).toString())
            else
                otp.set("")


            //resendOTPType.set(String.getNavigator()?.getMessage(R.string.resend_otp))
            val text: String =
                java.lang.String.format(getNavigator()?.getMessage(R.string.resend_otp)!!, type)
            resendOTPType.set(text)
            if (bundle?.getBoolean(Constants.CHANGE_LANGUAGE) == true) {
                if (bundle.getLong(REMAINING_TIME) > 0) {
                    timeOver.set(false)
                    getNavigator()?.showTimer(bundle.getLong(REMAINING_TIME))
                } else {
                    timeOver.set(true)
                    getNavigator()?.updateOTPView()
                    resendOTPType.set(getNavigator()?.getMessage(R.string.resend))
                }
            } else {
                getNavigator()?.showTimer(Constants.RESEND_OTP_TIME)
                timeOver.set(false)
            }

        }
    }

    fun changeNumber(v: View) {
        getNavigator()?.sendChangeMobileNoMoEngageEvent()
        getNavigator()?.openAndFinishActivity(LoginActivity::class.java, Bundle().apply {
            putString(Constants.MOBILE_NO, mobileNo.get())
        })

    }

    fun resendOTP() = viewModelScope.launch {
        getNavigator()?.sendResendOTPMoEngageEvent(mobileNo.get().toString())
        val sendOtpRequestModel = SendOtpRequestModel()

        sendOtpRequestModel.phone = mobileNo.get().toString()
        sendOtpRequestModel.retryType = type
        sendOtpRequestModel.otp_reference_id = otpReference.get()?.toInt()
        sendOTPCall(sendOtpRequestModel)

    }

    private suspend fun sendOTPCall(sendOtpRequestModel: SendOtpRequestModel) {

        try {

            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.resendOTP(sendOtpRequestModel)


                if (response.isSuccessful) {
                    val sendOtpResponseModel = response.body()
                    if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {
                        val text: String =
                            java.lang.String.format(
                                getNavigator()?.getMessage(R.string.resend_otp)!!,
                                type.uppercase()
                            )
                        resendOTPType.set(text)
                        otp.set("")
                        timeOver.set(false)

                        getNavigator()?.showTimer(Constants.RESEND_OTP_TIME)
                        //getNavigator()?.showToast(sendOtpResponseModel?.gp_api_message)
                    } else {
                        getNavigator()?.showToast(sendOtpResponseModel?.gp_api_message)
                    }
                } else if (response.code() == Constants.UNAUTHORIZED) {
                    getNavigator()?.getInitModel()?.let { refreshToken(it, isValidate = false) }
                } else {
                    val text: String =
                        java.lang.String.format(
                            getNavigator()?.getMessage(R.string.resend)!!,
                            type.uppercase()
                        )
                    resendOTPType.set(text)
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                }

            } else
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    private fun handleResendOTPResponse(response: Response<SendOtpResponseModel>): ApiResponse<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun onLanguageClick() {
        getNavigator()?.onLanguageChangeClick()

    }

    fun updateLanguage() = viewModelScope.launch {

        val updateLanguageRequestModel = UpdateLanguageRequestModel(getNavigator()?.getLanguage()!!)
        updateLanguage(updateLanguageRequestModel)
    }


    suspend fun updateLanguage(sendOtpRequestModel: UpdateLanguageRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response =
                    onBoardingRepository.updateLanguageWhileOnBoarding(sendOtpRequestModel)

                val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                if (Constants.GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                    getNavigator()?.sendLanguageUpdateMoEngageEvent()
                    getNavigator()?.openAndFinishActivity(
                        VerifyOtpActivity::class.java,
                        Bundle().apply {
                            putString(Constants.MOBILE_NO, mobileNo.get())
                            putString(Constants.Otp, otp.get())
                            putBoolean(Constants.CHANGE_LANGUAGE, true)
                            putLong(Constants.REMAINING_TIME, remaningDuration)
                            putInt(Constants.OTP_REFERENCE, otpReference.get()?.toInt()!!)
                        })
                } else {
                    getNavigator()?.showToast(updateLanguageResponseModel?.gp_api_message)
                }

            } else
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)

            }
        }
    }

    private fun handleLanguageUpdateResponse(response: Response<UpdateLanguageResponseModel>): ApiResponse<UpdateLanguageResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun onResendClicked(v: View) {
        when (v.id) {
            R.id.tvSMS -> {
                type = Constants.SMS
                resendOTP()
            }

            R.id.tvCall -> {
                type = Constants.VOICE
                resendOTP()

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

    private fun refreshToken(
        initiateAppDataRequestModel: InitiateAppDataRequestModel,
        isValidate: Boolean
    ) {

        viewModelScope.launch {
            getNavigator()?.onLoading()
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = onBoardingRepository.getInitialData(initiateAppDataRequestModel)
                    if (response.isSuccessful) {
                        val initiateAppDataResponseModel = response.body()

                        if (Constants.GP_API_STATUS.equals(initiateAppDataResponseModel?.gp_api_status)) {
                            SharedPreferencesHelper.instance?.putString(
                                SharedPreferencesKeys.session_token,
                                initiateAppDataResponseModel?.gp_api_response_data?.temp_token
                            )

                            if (isValidate) {
                                submitOtp()
                            } else {
                                resendOTP()
                            }
                        } else {
                            getNavigator()?.onError(initiateAppDataResponseModel?.gp_api_message)
                        }
                    } else {
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }

    }

}