package agstack.gramophone.ui.language.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.utils.Constants.GP_API_STATUS
import agstack.gramophone.utils.Resource
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
class LanguageViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : BaseViewModel<LanguageActivityNavigator>() {

    val registerDeviceModel: MutableLiveData<Resource<InitiateAppDataResponseModel>> =
        MutableLiveData()
    var updateLanguage: MutableLiveData<LanguageData> = MutableLiveData()


    fun getDeviceToken(initiateAppDataRequestModel:  InitiateAppDataRequestModel) = viewModelScope.launch {
        getToken(initiateAppDataRequestModel)
    }

    private suspend fun getToken(initiateAppDataRequestModel:  InitiateAppDataRequestModel) {

        registerDeviceModel.postValue(Resource.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getDeviceToken(initiateAppDataRequestModel)
                val initiateAppDataResponseModel = handleOrderResponse(response).data

                if (GP_API_STATUS.equals(initiateAppDataResponseModel?.gp_api_status)) {
                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.session_token,
                        initiateAppDataResponseModel?.gp_api_response_data?.temp_token
                    )
                    registerDeviceModel.postValue(Resource.Success(initiateAppDataResponseModel!!))
                }
            } else
            registerDeviceModel.postValue(Resource.Error("No Internet Connection"))
      } catch (ex: Exception) {
         when (ex) {
            is IOException -> registerDeviceModel.postValue(Resource.Error("Network Failure"))
            else -> registerDeviceModel.postValue(Resource.Error("Conversion Error"))
         }
      }
    }

    private fun handleOrderResponse(response: Response<InitiateAppDataResponseModel>): Resource<InitiateAppDataResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun onLanguageUpdate(v:View) {
       getNavigator()?.moveToNext()
    }
}