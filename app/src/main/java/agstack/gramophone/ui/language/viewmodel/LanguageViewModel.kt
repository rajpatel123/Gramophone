package agstack.gramophone.ui.language.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.GpApiResponseData
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants.GP_API_STATUS
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.view.View
import androidx.databinding.ObservableField
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
    var language: Language? = null
    var isLanguageSelected = ObservableField<Boolean>()

    fun getLanguageList() = viewModelScope.launch {
        isLanguageSelected.set(false)
        getLanguage()
    }

    private suspend fun getLanguage() {
        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getLanguage()
                val languageData = handleLanguageResponse(response).data

                if (GP_API_STATUS.equals(languageData?.gp_api_status)) {
                    SharedPreferencesHelper.instance?.putParcelable(
                        SharedPreferencesKeys.languageList,
                        languageData?.gp_api_response_data!!
                    )

                    getNavigator()?.updateLanguageList(LanguageAdapter(languageData?.gp_api_response_data?.language_list!!)){
                        language=it
                        isLanguageSelected.set(true)
                    }


                }else{
                    getNavigator()?.onError(languageData?.gp_api_message)
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

        getNavigator()?.onLoading()
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
                        SharedPreferencesKeys.CustomerSupportNo,
                        initiateAppDataResponseModel?.gp_api_response_data?.help_data_list?.customer_support_no
                    )
                    SharedPreferencesHelper.instance?.putParcelable(
                        SharedPreferencesKeys.app_data,
                        initiateAppDataResponseModel!!
                    )
                    getNavigator()?.moveToNext()
                }else{
                    getNavigator()?.onError(initiateAppDataResponseModel?.gp_api_message)

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
        when (v.id) {
            R.id.btnContinue -> {
                if (language != null) {
                    val langIsoCode = language?.language_code
                    if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                        LocaleManagerClass.updateLocale(langIsoCode, getNavigator()?.getResource())
                    }
                    getNavigator()?.initiateApp()
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.select_language_msg_english))
                }
            }
            R.id.btnSave -> {
                if (language != null) {
                    val langIsoCode = language?.language_code
                    if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                        LocaleManagerClass.updateLocale(langIsoCode, getNavigator()?.getResource())
                    }

                    val updateLanguageRequestModel = UpdateLanguageRequestModel(getNavigator()?.getLanguage()!!)
                    viewModelScope.launch {
                        updateLanguage(updateLanguageRequestModel)
                    }
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.select_language_msg_english))
                }
            }
        }


    }

    fun populateLanguage() {
        var gpApiResponseData =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.languageList, GpApiResponseData::class.java
            )
        var languageAdapter = LanguageAdapter(gpApiResponseData?.language_list!!)
        getNavigator()?.updateLanguageList(languageAdapter) {
            language = it
        }

        val languageCode = getNavigator()?.getLanguageCode()
        if (!languageCode.isNullOrEmpty()) {
            languageAdapter.mLanguageList.forEach {
                if (it.language_code.equals(languageCode.toString(), true)) {
                    it.selected = true
                    language=it
                }else{
                    it.selected = false
                }
                language=it
            }
            language?.let {
                languageAdapter.selectedLanguage?.invoke(it)
                languageAdapter.notifyDataSetChanged()

            }


        }
    }

    suspend fun updateLanguage(sendOtpRequestModel: UpdateLanguageRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.updateLanguageWhileOnBoarding(sendOtpRequestModel)

                val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                if (GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                    getNavigator()?.onSuccess(updateLanguageResponseModel?.gp_api_message)
                    getNavigator()?.closeLanguageList()
                } else {
                    getNavigator()?.onError(updateLanguageResponseModel?.gp_api_message)
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

    private fun handleLanguageUpdateResponse(response: Response<UpdateLanguageResponseModel>): ApiResponse<UpdateLanguageResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

}