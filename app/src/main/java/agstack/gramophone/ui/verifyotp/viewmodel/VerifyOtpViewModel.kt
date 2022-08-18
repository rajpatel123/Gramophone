package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.view.StateListActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<VerifyOTPNavigator>() {
    var otp: String = ""
    var otpHint: String = ""
    var mobileNo = ObservableField<String>()
    var otpReference = ObservableField<String>()

    var time: String = ""
    var timeOver: Boolean = false


    val validateOtpResponseModel: MutableLiveData<ApiResponse<ValidateOtpResponseModel>> =
        MutableLiveData()

    fun submitOtp(v: View) = viewModelScope.launch {
        if (otp.isNullOrEmpty()) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_otp)!!)
        } else if (otp.length < 6) {
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.please_enter_6_digit_otp)!!)
        } else {
            val validateOtpRequestModel = ValidateOtpRequestModel(
                mobileNo.get().toString(), otp,
                otpReference.get()?.toInt()!!
            )
            validateOtp(validateOtpRequestModel)
        }
    }

    private suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel) {

        validateOtpResponseModel.postValue(ApiResponse.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.validateOtp(validateOtpRequestModel)
                val responseData = handleOrderResponse(response).data

                if (Constants.GP_API_STATUS.equals(responseData?.gp_api_status)) {
                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.session_token,
                        responseData?.gp_api_response_data?.token
                    )

                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.UUIdKey,
                        responseData?.gp_api_response_data?.uuid
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
                        getNavigator()?.openAndFinishActivity(HomeActivity::class.java)
                    } else {
                        getNavigator()?.openAndFinishActivity(StateListActivity::class.java)
                    }
                    getNavigator()?.showToast(responseData?.gp_api_message)
                } else {
                    getNavigator()?.showToast(responseData?.gp_api_message)

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
                getNavigator()?.getMessage(R.string.otp_hint) + " " + bundle?.getString(Constants.MOBILE_NO)
                    .toString()
            mobileNo.set(bundle?.getString(Constants.MOBILE_NO).toString())
            otpReference.set(bundle?.getInt(Constants.OTP_REFERENCE).toString())
        }
        getNavigator()?.showTimer()
    }

    fun changeNumber(v: View) {
        getNavigator()?.openAndFinishActivity(LoginActivity::class.java, Bundle().apply {
            putString(Constants.MOBILE_NO,mobileNo.get())
        })

    }

    fun resendOTP(v: View) = viewModelScope.launch {

        val sendOtpRequestModel = SendOtpRequestModel()

        sendOtpRequestModel.phone = mobileNo.get().toString()
        sendOtpRequestModel.retryType = Constants.SMS
        sendOtpRequestModel.otp_reference_id = otpReference.get()?.toInt()
        sendOTPCall(sendOtpRequestModel)

    }

    private suspend fun sendOTPCall(sendOtpRequestModel: SendOtpRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.resendOTP(sendOtpRequestModel)

                val sendOtpResponseModel = handleResendOTPResponse(response).data

                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {
                    getNavigator()?.showTimer()
                    getNavigator()?.showToast(sendOtpResponseModel?.gp_api_message)
                } else {
                    getNavigator()?.showToast(sendOtpResponseModel?.gp_api_message)
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


    fun onHelpClick(v: View) {
        var initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )
        if (getNavigator()?.requestPermission(Manifest.permission.CALL_PHONE) == true)
            getNavigator()?.onHelpClick(initiateAppDataResponseModel?.gp_api_response_data?.help_data_list?.customer_support_no!!)
    }

    fun onLanguageClick(v: View) {
        getNavigator()?.onLanguageChangeClick()

    }

    fun updateLanguage() = viewModelScope.launch {

        val updateLanguageRequestModel = UpdateLanguageRequestModel(getNavigator()?.getLanguage()!!)
        updateLanguage(updateLanguageRequestModel)
    }


    suspend fun updateLanguage(sendOtpRequestModel: UpdateLanguageRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.updateLanguage(sendOtpRequestModel)

                val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                if (Constants.GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                    getNavigator()?.showToast(updateLanguageResponseModel?.gp_api_message)
                    getNavigator()?.restartActivity(Bundle().apply {
                        putString(Constants.MOBILE_NO, mobileNo.get())
                        putInt(Constants.OTP_REFERENCE, otpReference.get()?.toInt()!!)
                    })
                } else {
                    getNavigator()?.showToast(updateLanguageResponseModel?.gp_api_message)
                }

            } else
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            getNavigator()?.restartActivity(Bundle().apply {
                putString(Constants.MOBILE_NO, mobileNo.get())
                putInt(
                    Constants.OTP_REFERENCE,
                    getNavigator()?.getBundle()?.getInt(Constants.OTP_REFERENCE)!!
                )
            })
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
}