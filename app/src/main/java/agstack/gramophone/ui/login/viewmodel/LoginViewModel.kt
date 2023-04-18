package agstack.gramophone.ui.login.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.utils.*
import android.Manifest
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.google.zxing.integration.android.IntentResult
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<LoginNavigator>() {
    var mobileNo = ObservableField<String>()
    var referralCode = ObservableField<String>()
    var isReferralCodeApplied = ObservableField<Boolean>()
    var referralCodeValue: String? = null
    var mAlertDialog: AlertDialog? = null

    fun sendOTP() = viewModelScope.launch {
        if (mobileNo.get().isNullOrEmpty()) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.enter_mobile_lebel)!!)
        } else if (mobileNo.get()?.length!! < 10) {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.enter_10_digit_mob)!!)
        } else {
            val sendOtpRequestModel = SendOtpRequestModel()

            sendOtpRequestModel.phone = mobileNo.get()
            sendOtpRequestModel.language = getNavigator()?.getLanguage()
            if (!referralCodeValue.isNullOrEmpty()) {
                sendOtpRequestModel.referral_code = referralCodeValue
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
                getNavigator()?.hideProgressBar()
                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {

                    val loginData = handleLoginResponse(response)
                    getNavigator()?.sendOtpSentMoEngageEvent(mobileNo.get()!!)
                    getNavigator()?.moveToNext(Bundle().apply {
                        putString(Constants.MOBILE_NO, mobileNo.get())
                        putString(Constants.REFERRAL_CODE, referralCode.get())
                        putString(Constants.REFERRAL_CODE_VALUE, referralCodeValue)
                        putInt(
                            Constants.OTP_REFERENCE,
                            loginData.data?.gp_api_response_data?.otp_reference_id!!
                        )
                    })

                    try {
                        val properties = Properties()
                        properties.addAttribute("Source_Screen", "Login")
                            .addAttribute("App Version", BuildConfig.VERSION_NAME)
                            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
                            .setNonInteractive()
                        getNavigator()?.sendMoEngageEvent("KA_Edit_OTP_Generated",properties)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))

                }

            } else
                getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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

    fun onLanguageClick() {
        getNavigator()?.onLanguageChangeClick()

    }



    fun onPrivacyClicked() {
        getNavigator()?.sendPrivacyMoEngageEvent()
        val initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )
        getNavigator()?.openWebView(Bundle().apply {
            putString(
                Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.privacy_policy_url
            )
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.privacy))
        })
    }

    fun onTermsClicked() {
        val initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )

        getNavigator()?.sendTermsMoEngageEvent()
        getNavigator()?.openWebView(Bundle().apply {
            putString(
                Constants.PAGE_TITLE,
                getNavigator()?.getMessage(R.string.terms_n_conditions)
            )
            putString(
                Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.terms_of_service_url
            )
        })

    }

    fun onTermsOfUseClicked() {
        var initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )
        getNavigator()?.sendTermsMoEngageEvent()
        getNavigator()?.openWebView(Bundle().apply {
            putString(
                Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.referral_terms_conditions_url
            )
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.terms_of_use))

        })

    }

    fun onReferralCodeClicked() {
        getNavigator()?.openReferralDialog()
    }


    fun onRemoveReferralClicked() {
        referralCode.set("")
        referralCodeValue = ""
        isReferralCodeApplied.set(false)
        getNavigator()?.referralCodeRemoved()


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
                        LoginActivity::class.java,
                        Bundle().apply {
                            putString(Constants.MOBILE_NO, mobileNo.get())
                            putString(Constants.REFERRAL_CODE, referralCodeValue)
                        })
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                }

            } else
                getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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

    fun setMobileNumber() {
        if (!getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO).isNullOrEmpty()) {
            mobileNo.set(getNavigator()?.getBundle()?.getString(Constants.MOBILE_NO))
        } else if (!getNavigator()?.getMobileBundle()?.getString(Constants.MOBILE_NO)
                .isNullOrEmpty()
        ) {
            mobileNo.set(getNavigator()?.getMobileBundle()?.getString(Constants.MOBILE_NO))
        } else {
            getNavigator()?.showMobileNumberHint()
        }

        if (!getNavigator()?.getBundle()?.getString(Constants.REFERRAL_CODE).isNullOrEmpty()) {
            referralCode.set(
                getNavigator()?.getMessage(R.string.referral_code) + getNavigator()?.getBundle()?.getString(Constants.REFERRAL_CODE) + getNavigator()?.getMessage(
                    R.string.applied
                ))
            referralCodeValue=getNavigator()?.getBundle()?.getString(Constants.REFERRAL_CODE)
            isReferralCodeApplied.set(true)
        }
    }

    fun setDialog(mDialog: AlertDialog?) {
        mAlertDialog = mDialog
    }

    fun applyCode() {
        if (referralCode.get().isNotNull() && referralCode.get()?.length!! > 0) {
            referralCodeValue = referralCode.get()
            referralCode.set(
                getNavigator()?.getMessage(R.string.referral_code) + referralCode.get() + getNavigator()?.getMessage(
                    R.string.applied
                )
            )
            isReferralCodeApplied.set(true)
            mAlertDialog?.dismiss()
        } else {
            getNavigator()?.showToast(R.string.enter_referral_code)
        }

    }

    fun closeDialog() {
        mAlertDialog?.dismiss()
    }

    fun scanQR() {
        getNavigator()?.scanQR()
        mAlertDialog?.dismiss()
    }

    fun setReferralCodeFromQR(referralCodeValue: String) {
        isReferralCodeApplied.set(true)
        referralCode.set(
            getNavigator()?.getMessage(R.string.referral_code) +referralCodeValue + getNavigator()?.getMessage(
                R.string.applied))
    }
}



