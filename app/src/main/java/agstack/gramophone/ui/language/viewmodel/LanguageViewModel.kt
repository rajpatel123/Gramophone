package agstack.gramophone.ui.language.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.DeviceDetails
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.webview.view.WebViewActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants.GP_API_STATUS
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Intent
import android.os.Build
import android.provider.Settings
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
class LanguageViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : BaseViewModel<LanguageActivityNavigator>() {
    var language: Language? = null
    val registerDeviceModel: MutableLiveData<ApiResponse<InitiateAppDataResponseModel>> =
        MutableLiveData()


    val initiateAppDataRequestModel: InitiateAppDataRequestModel
        get() {
            val android_id = Settings.Secure.getString(getNavigator()?.getContentResolver(),
                Settings.Secure.ANDROID_ID)

            var deviceDetails = DeviceDetails(
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                android_id,
                Build.MODEL,
                Build.VERSION.SDK_INT.toString()
            )
            var registerDeviceRequestModel =
                getNavigator()?.let {
                    InitiateAppDataRequestModel(deviceDetails,
                        it.getLanguage(),)
                }



            return registerDeviceRequestModel!!

        }
    fun getLanguageList() = viewModelScope.launch {
        getLanguage()
    }

    private suspend fun getLanguage() {
        registerDeviceModel.postValue(ApiResponse.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getLanguage()
                val languageData = handleLanguageResponse(response).data

                if (GP_API_STATUS.equals(languageData?.gp_api_status)) {
                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.languageList,
                        Gson().toJson(languageData?.gp_api_response_data)
                    )
                    getNavigator()?.updateLanguageList(LanguageAdapter(languageData?.gp_api_response_data?.language_list!!)){
                        language=it
                    }
                }else{
                    //TODO handle error case
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

    fun initiateAppData(initiateAppDataRequestModel: InitiateAppDataRequestModel) =
        viewModelScope.launch {
            getInitialData(initiateAppDataRequestModel)
        }

    private suspend fun getInitialData(initiateAppDataRequestModel:  InitiateAppDataRequestModel) {

        registerDeviceModel.postValue(ApiResponse.Loading())
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getInitialData(initiateAppDataRequestModel)
                val initiateAppDataResponseModel = handleOrderResponse(response).data

                if (GP_API_STATUS.equals(initiateAppDataResponseModel?.gp_api_status)) {
                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.session_token,
                        initiateAppDataResponseModel?.gp_api_response_data?.temp_token
                    )

                    SharedPreferencesHelper.instance?.putString(
                        SharedPreferencesKeys.app_data,
                        Gson().toJson(initiateAppDataResponseModel)
                    )
                    getNavigator()?.moveToNext()
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

    private fun handleOrderResponse(response: Response<InitiateAppDataResponseModel>): ApiResponse<InitiateAppDataResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    private fun handleLanguageResponse(response: Response<LanguageListResponse>): ApiResponse<LanguageListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun onLanguageUpdate(v:View) {
        if (language!=null){
            val langIsoCode = language?.language_code
            if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                LocaleManagerClass.updateLocale(langIsoCode, getNavigator()?.getResource())
            }
            getNavigator()?.initiateApp()
        }else{
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.select_language_msg_english))
        }

    }



}