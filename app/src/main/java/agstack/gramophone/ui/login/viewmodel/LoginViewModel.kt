package agstack.gramophone.ui.login.viewmodel


import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.repository.LoginRepository
import agstack.gramophone.utils.*
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<LoginNavigator>() {
    var mobileNo :String?=""
    var referralCode :String?=""

    val generateOtpResponseModel: MutableLiveData<Resource<GenerateOtpResponseModel>> =
        MutableLiveData()

    fun sendOTP(v: View) = viewModelScope.launch {
        if (mobileNo.isNullOrEmpty()){
            generateOtpResponseModel.postValue(Resource.Error()
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
            if (hasInternetConnection(context)) {

                val response = onboardingRepository.sendOTP(loginMap)
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = loginRepository.sendOTP(loginMap)
                generateOtpResponseModel.postValue(handleOrderResponse(response))
            } else
                generateOtpResponseModel.postValue(Resource.Error("No Internet Connection"))
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> generateOtpResponseModel.postValue(Resource.Error("Network Failure"))
                else -> generateOtpResponseModel.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleOrderResponse(response: Response<GenerateOtpResponseModel>): Resource<GenerateOtpResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
