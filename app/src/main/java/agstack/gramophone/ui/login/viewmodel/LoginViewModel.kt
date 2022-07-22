package agstack.gramophone.ui.login.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import android.os.Bundle
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
class LoginViewModel @Inject constructor(
    private val onBoardingRepository : OnBoardingRepository
) : BaseViewModel<LoginNavigator>() {
    var mobileNo :String?=""
    var referralCode :String?=null

    val generateOtpResponseModel: MutableLiveData<ApiResponse<SendOtpResponseModel>> =
        MutableLiveData()

    fun sendOTP(v: View) = viewModelScope.launch {
        if (mobileNo.isNullOrEmpty()){
            generateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.enter_mobile_lebel)!!))
        }else{
           val sendOtpRequestModel= SendOtpRequestModel()

            sendOtpRequestModel.phone = mobileNo
            sendOtpRequestModel.language = getNavigator()?.getLanguage()
            if (!referralCode.isNullOrEmpty()){
                sendOtpRequestModel.referral_code = referralCode
            }

            sendOTPCall(sendOtpRequestModel)
        }
    }

    private suspend fun sendOTPCall(sendOtpRequestModel: SendOtpRequestModel) {

        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.sendOTP(sendOtpRequestModel)

                val sendOtpResponseModel = handleLoginResponse(response).data

                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {

                    var loginData = handleLoginResponse(response)
                    val bundle = Bundle()
                    //Add your data from getFactualResults method to bundle
                    bundle.putString(Constants.MOBILE_NO,mobileNo)
                    bundle.putInt(Constants.OTP_REFERENCE,
                        loginData.data?.gp_api_response_data?.otp_reference_id!!
                    )

                    getNavigator()?.moveToNext(bundle)
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

    private fun handleLoginResponse(response: Response<SendOtpResponseModel>): ApiResponse<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun onHelpClick(v:View) {
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

    fun onPrivacyClicked(v:View){
        var initiateAppDataResponseModel = Gson().fromJson(
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.app_data
            ), InitiateAppDataResponseModel::class.java
        )

        val bundle = Bundle()
        bundle.putString(Constants.PAGE_URL, initiateAppDataResponseModel.gp_api_response_data.external_link_list.privacy_policy_url)
        bundle.putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.privacy))
        getNavigator()?.openWebView(bundle)
    }

    fun onTermsClicked(v:View){
        var initiateAppDataResponseModel = Gson().fromJson(
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.app_data
            ), InitiateAppDataResponseModel::class.java
        )

        val bundle = Bundle()
        bundle.putString(
            Constants.PAGE_URL,
            initiateAppDataResponseModel.gp_api_response_data.external_link_list.terms_of_service_url
        )

        getNavigator()?.openWebView(Bundle().apply {
            putString(
                Constants.PAGE_TITLE,
                getNavigator()?.getMessage(R.string.terms_n_conditions)
            )
        })

    }

    fun onTermsOfUseClicked() {
        var initiateAppDataResponseModel = Gson().fromJson(
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.app_data
            ), InitiateAppDataResponseModel::class.java
        )

        val bundle = Bundle()
        bundle.putString(
            Constants.PAGE_URL,
            initiateAppDataResponseModel.gp_api_response_data.external_link_list.referral_terms_conditions
        )
        bundle.putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.terms_of_use))
        getNavigator()?.openWebView(bundle)

    }

    fun onReferralCodeClicked() {
      getNavigator()?.openReferralDialog()
    }


    fun onRemoveReferralClicked() {
        referralCode = null
        getNavigator()?.referralCodeRemoved()


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


