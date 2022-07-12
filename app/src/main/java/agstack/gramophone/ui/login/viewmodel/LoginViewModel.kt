package agstack.gramophone.ui.login.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ApiResponse
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    var referralCode :String?=""

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

        generateOtpResponseModel.postValue(ApiResponse.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.sendOTP(sendOtpRequestModel)

                val sendOtpResponseModel = handleOrderResponse(response).data

                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {
                    generateOtpResponseModel.postValue(handleOrderResponse(response))
                }

            } else
                generateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.no_internet)!!))
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> generateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.network_failure)!!))
                else -> generateOtpResponseModel.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!))
            }
        }
    }

    private fun handleOrderResponse(response: Response<SendOtpResponseModel>): ApiResponse<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }
}
