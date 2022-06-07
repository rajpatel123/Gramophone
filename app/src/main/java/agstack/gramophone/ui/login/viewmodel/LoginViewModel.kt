package agstack.gramophone.ui.login


import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.login.repository.LoginRepository
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.hasInternetConnection
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val generateOtpResponseModel: MutableLiveData<Resource<GenerateOtpResponseModel>> =
        MutableLiveData()

    fun sendOTP(loginMap: HashMap<Any, Any>) = viewModelScope.launch {
        sendOTPCall(loginMap)
    }

    private suspend fun sendOTPCall(loginMap: HashMap<Any, Any>) {

        generateOtpResponseModel.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
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
