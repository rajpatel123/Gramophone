package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.settings.WhatsappNavigator
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.moengage.core.Properties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WhatsAppOptINOutViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository

) : BaseViewModel<WhatsappNavigator>() {
    var optINOut = ObservableField<String>()
    private var optInOutJob: Job? = null
    var progressBar = ObservableField<Boolean>()

    fun onOptInClick() {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.WHATSAPP_OPT_IN) == true) {
            optWhatsapp("opt-out")
        } else {
            optWhatsapp("opt-in")
        }
    }

    private fun optWhatsapp(type: String) {
        optInOutJob?.takeIf { it.isActive }?.cancel()
        progressBar.set(true)
        optInOutJob = viewModelScope.launch {
            if (getNavigator()?.isNetworkAvailable() == true) {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {

                        val optInOutResponse = settingsRepository.optInOutForWhatsappUpdates(type)

                        val optInResponseData = handleResponse(optInOutResponse).data

                        if (Constants.GP_API_STATUS.equals(optInResponseData?.gp_api_status)) {
                            SharedPreferencesHelper.instance?.putBoolean(
                                SharedPreferencesKeys.WHATSAPP_OPT_IN,
                                optInResponseData?.gp_api_response_data?.whatsapp_optin
                            )
                            progressBar.set(false)

                            getNavigator()?.onSuccess(optInResponseData?.gp_api_message)
                            val properties = Properties()
                                .addAttribute(
                                    "App Profile ID",
                                    SharedPreferencesHelper.instance?.getString(
                                        SharedPreferencesKeys.CUSTOMER_ID
                                    )!!
                                )
                                .addAttribute(
                                    "mobile number",
                                    SharedPreferencesHelper.instance?.getString(
                                        SharedPreferencesKeys.USER_MOBILE
                                    )!!
                                )
                                .addAttribute("App Version", BuildConfig.VERSION_NAME)
                                .addAttribute("SDK Version", Build.VERSION.SDK_INT)
                                .setNonInteractive()
                            if ("opt-out".equals(type)) {
                                SharedPreferencesHelper.instance?.putBoolean(
                                    SharedPreferencesKeys.WHATSAPP_OPT_IN,
                                    false
                                )

                                getNavigator()?.sendMoEngageEvent("KA_WhatsApp_Opt_Out", properties)
                            } else {
                                SharedPreferencesHelper.instance?.putBoolean(
                                    SharedPreferencesKeys.WHATSAPP_OPT_IN,
                                    true
                                )
                                getNavigator()?.sendMoEngageEvent("KA_WhatsApp_Opt_In", properties)
                            }
                        } else {
                            getNavigator()?.onError(optInResponseData?.gp_api_message)
                        }
                    } else
                        getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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

    fun updateAction() {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.WHATSAPP_OPT_IN) == true){
            optINOut.set(getNavigator()?.getMessage(R.string.opt_out))
        }else{
            optINOut.set(getNavigator()?.getMessage(R.string.opt_in_now))
        }
    }

}