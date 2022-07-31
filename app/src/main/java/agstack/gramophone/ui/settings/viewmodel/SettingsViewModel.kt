package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.GpApiResponseData
import agstack.gramophone.ui.settings.SettingsNavigator
import agstack.gramophone.ui.settings.view.LanguageUpdateActivity
import agstack.gramophone.ui.settings.view.WhatsAppOptInOptOutActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<SettingsNavigator>() {

    var languageName = ObservableField<String>()

    fun onLanguageClicked() {
        getNavigator()?.openActivity(LanguageUpdateActivity::class.java, null)
    }

    fun onWhatsAppClicked() {
        getNavigator()?.openActivity(WhatsAppOptInOptOutActivity::class.java, null)
    }

    fun setLanguageName() {
        val languageCode = getNavigator()?.getLanguageCode()
        var gpApiResponseData =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.languageList, GpApiResponseData::class.java
            )

        gpApiResponseData?.let { languageData ->
            languageData.language_list.forEach {
                if (it.language_code.equals(languageCode, true)) {
                    languageName.set(it.language)
                }
            }
        }
    }

    fun onTermsClick(){
        var initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data
                , InitiateAppDataResponseModel::class.java
            )


        getNavigator()?.openWebView(Bundle().apply {
            putString(Constants.PAGE_URL, initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.terms_of_service_url)
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.terms_of_service))
        })
    }

    fun onPrivacyClick(){
        var initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data
                , InitiateAppDataResponseModel::class.java
            )


        getNavigator()?.openWebView(Bundle().apply {
            putString(Constants.PAGE_URL, initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.privacy_policy_url)
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.privacy))
        })
    }

}