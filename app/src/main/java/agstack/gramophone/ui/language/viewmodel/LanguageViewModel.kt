package agstack.gramophone.ui.language.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.GpApiResponseData
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.utils.*
import agstack.gramophone.utils.Constants.GP_API_STATUS
import android.app.AlertDialog
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
    var push_message = ObservableField<String>()
lateinit var mAlertDialog: AlertDialog
    fun getLanguageList() = viewModelScope.launch {
        isLanguageSelected.set(false)
        getNavigator()?.initiateApp()
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
                val initiateAppDataResponseModel: InitiateAppDataResponseModel? = handleOrderResponse(response).data

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

                    push_message.set(initiateAppDataResponseModel?.gp_api_response_data?.notifi_messages)
                    getNavigator()?.showPushPermissionDialog()
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
                  viewModelScope.launch {
                      try {
                          if (getNavigator()?.isNetworkAvailable() == true) {
                              val updateLanguageRequestModel = UpdateLanguageRequestModel(language!!.language_code)

                              val response = onBoardingRepository.updateLanguageWhileOnBoarding(updateLanguageRequestModel)

                              val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                              if (GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                                  // getNavigator()?.onSuccess(updateLanguageResponseModel?.gp_api_message)
                                  SharedPreferencesHelper.instance?.putParcelable(
                                      SharedPreferencesKeys.BANNER_DATA,
                                      BannerResponse("")
                                  )

                                  val langIsoCode = language?.language_code
                                  SharedPreferencesHelper.instance?.putString(SharedPreferencesKeys.languageCode,langIsoCode)
                                  if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                                      LocaleManagerClass.updateLocale(langIsoCode, getNavigator()?.getResource())
                                  }

                                  SharedPreferencesHelper.instance?.putBoolean(
                                      SharedPreferencesKeys.LANGUAGE_UPDATE,
                                    true
                                  )
                                  getNavigator()?.moveToNext()
                              } else {
                                  getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.please_select_language))
                }
            }
            R.id.btnSave -> {
                if (language != null) {
                    val updateLanguageRequestModel = UpdateLanguageRequestModel(language!!.language_code)
                    viewModelScope.launch {
                        updateLanguage(updateLanguageRequestModel)
                    }
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.please_select_language))
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
                val response = onBoardingRepository.updateLanguage(sendOtpRequestModel)

                val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                if (GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                   // getNavigator()?.onSuccess(updateLanguageResponseModel?.gp_api_message)
                    SharedPreferencesHelper.instance?.putParcelable(
                        SharedPreferencesKeys.BANNER_DATA,
                        BannerResponse("")
                    )

                    val langIsoCode = language?.language_code
                    SharedPreferencesHelper.instance?.putString(SharedPreferencesKeys.languageCode,langIsoCode)
                    if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                        LocaleManagerClass.updateLocale(langIsoCode, getNavigator()?.getResource())
                    }

                    SharedPreferencesHelper.instance?.putBoolean(
                        SharedPreferencesKeys.LANGUAGE_UPDATE,
                        true
                    )
                    getNavigator()?.closeLanguageList()
                } else {
                    getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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

     fun getSecretKeys() {
         viewModelScope.launch {
             try {
                 if (getNavigator()?.isNetworkAvailable() == true) {
                     val response = onBoardingRepository.getSecretKeys()


                     if (response.isSuccessful && GP_API_STATUS.equals(response?.body()?.gp_api_status)) {
                         // getNavigator()?.onSuccess(updateLanguageResponseModel?.gp_api_message)

                         SharedPreferencesHelper.instance!!.putString(SharedPreferencesKeys.GOOGLE_API_KEY, response?.body()?.gp_api_response_data?.googleApi)
                         SharedPreferencesHelper.instance!!.putString(SharedPreferencesKeys.ADDRESS_API, response?.body()?.gp_api_response_data?.placeApi)
                     } else {
                         getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
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

    fun openSetting(){
        getNavigator()?.openNotificationSetting()
    }

    fun cancel(){
        mAlertDialog?.dismiss()
    }

    fun setDialog(mAlertDialog: AlertDialog?) {
        this.mAlertDialog = mAlertDialog!!

    }

}