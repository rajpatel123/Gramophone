package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.view.View
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
    var mobileNo: String = ""


    val validateOtpResponseModel: MutableLiveData<ApiResponse<ValidateOtpResponseModel>> =
        MutableLiveData()

    fun submitOtp(v: View) = viewModelScope.launch {
        if (otp.isNullOrEmpty()) {
            validateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.enter_mobile_lebel)!!))
        } else {
            val validateOtpRequestModel = ValidateOtpRequestModel(mobileNo, otp)
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

                        SharedPreferencesHelper.instance?.putBoolean(
                            SharedPreferencesKeys.logged_in,
                            true
                        )
                        validateOtpResponseModel.postValue(ApiResponse.Success(responseData!!))
                    }
                } else
                    validateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.no_internet)!!))
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> validateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.network_failure)!!))
                    else -> validateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!))
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


    }