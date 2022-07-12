package agstack.gramophone.ui.login.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Resource
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

    val generateOtpResponseModel: MutableLiveData<Resource<SendOtpResponseModel>> =
        MutableLiveData()

    fun sendOTP(v: View) = viewModelScope.launch {
        if (mobileNo.isNullOrEmpty()){
            generateOtpResponseModel.postValue(Resource.Error(getNavigator()?.getMessage(R.string.enter_mobile_lebel)!!))
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

        generateOtpResponseModel.postValue(Resource.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.sendOTP(sendOtpRequestModel)

                val sendOtpResponseModel = handleOrderResponse(response).data

                if (Constants.GP_API_STATUS.equals(sendOtpResponseModel?.gp_api_status)) {
                    generateOtpResponseModel.postValue(handleOrderResponse(response))
                }

            } else
                generateOtpResponseModel.postValue(Resource.Error("No Internet Connection"))
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> generateOtpResponseModel.postValue(Resource.Error("Network Failure"))
                else -> generateOtpResponseModel.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleOrderResponse(response: Response<SendOtpResponseModel>): Resource<SendOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
