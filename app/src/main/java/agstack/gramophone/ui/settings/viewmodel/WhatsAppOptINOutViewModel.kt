package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.settings.WhatsappNavigator
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WhatsAppOptINOutViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository

) : BaseViewModel<WhatsappNavigator>() {

    fun onOptInClick() {
        optWhatsapp("opt-in")
    }

    private fun optWhatsapp(type: String) {
        viewModelScope.async {
            if (getNavigator()?.isNetworkAvailable() == true) {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val optInOutDeferred = async {
                            settingsRepository.optInOutForWhatsappUpdates(type)
                        }
                        val optInOutResponse = optInOutDeferred.await()


                        val optInResponseData = handleResponse(optInOutResponse).data

                        if (Constants.GP_API_STATUS.equals(optInResponseData?.gp_api_status)) {
                            getNavigator()?.onSuccess(optInResponseData?.gp_api_message)
                        } else {
                            getNavigator()?.onError(optInResponseData?.gp_api_message)
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
        }

    }


    private fun handleResponse(response: Response<WhatsAppOptInResponseModel>): ApiResponse<WhatsAppOptInResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

}