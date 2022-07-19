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
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
    var mobileNo: String = ""
    var time: String = ""
    var timeOver: Boolean = false


    val validateOtpResponseModel: MutableLiveData<ApiResponse<ValidateOtpResponseModel>> =
        MutableLiveData()

    fun submitOtp(v: View) = viewModelScope.launch {
        if (otp.isNullOrEmpty()) {
            validateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.please_enter_otp)!!))
        } else {
            val validateOtpRequestModel = ValidateOtpRequestModel(getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO).toString(), otp,
                getNavigator()?.getBundle()?.getInt(Constants.OTP_REFERENCE)!!
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
                        SharedPreferencesHelper.instance?.putBoolean(
                            SharedPreferencesKeys.logged_in,
                            true
                        )

                        if (responseData?.gp_api_response_data?.is_address == true){
                            getNavigator()?.moveToNext(HomeActivity::class.java)
                        }else{
                            getNavigator()?.moveToNext(StateListActivity::class.java)
                        }
                        getNavigator()?.onSuccess(responseData?.gp_api_message)
                    }
                }  else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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
        otpHint = getNavigator()?.getMessage(R.string.otp_hint)+" "+getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO).toString()
        getNavigator()?.showTimer()

    }

    fun changeNumber(v:View){
        getNavigator()?.changeNumber()
    }
    fun resendOTP(v: View) =  viewModelScope.launch {

            val sendOtpRequestModel= SendOtpRequestModel()

            sendOtpRequestModel.phone = getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO).toString()
            sendOtpRequestModel.retryType = "sms"
            //sendOtpRequestModel.language = getNavigator()?.getLanguage()
//            if (!referralCode.isNullOrEmpty()){
//                sendOtpRequestModel.referral_code = referralCode
//            }

            sendOTPCall(sendOtpRequestModel)

    }

    private suspend fun sendOTPCall(sendOtpRequestModel: SendOtpRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.resendOTP(sendOtpRequestModel)

                val sendOtpResponseModel = handleResendOTPResponse(response).data

                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {
                    getNavigator()?.showTimer()
                    getNavigator()?.onSuccess(sendOtpResponseModel?.gp_api_message)
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

    private fun handleResendOTPResponse(response: Response<SendOtpResponseModel>): ApiResponse<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }


    fun onHelpClick(v:View){
        var initiateAppDataResponseModel = Gson().fromJson(
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.app_data
            ), InitiateAppDataResponseModel::class.java
        )
        if (getNavigator()?.requestPermission(Manifest.permission.CALL_PHONE) == true)
            getNavigator()?.onHelpClick(initiateAppDataResponseModel.gp_api_response_data.help_data_list.customer_support_no)
    }

    fun onLanguageClick(v:View){
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
                    getNavigator()?.onSuccess(updateLanguageResponseModel?.gp_api_message)
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

    private fun handleLanguageUpdateResponse(response: Response<UpdateLanguageResponseModel>): ApiResponse<UpdateLanguageResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }
}