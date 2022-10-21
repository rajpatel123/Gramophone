package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.GpApiResponseData
import agstack.gramophone.ui.settings.SettingsNavigator
import agstack.gramophone.ui.settings.view.BlockedUsersActivity
import agstack.gramophone.ui.settings.view.LanguageUpdateActivity
import agstack.gramophone.ui.settings.view.WhatsAppOptInOptOutActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<SettingsNavigator>() {
    var initiateAppDataResponseModel: InitiateAppDataResponseModel? = null
    var languageName = ObservableField<String>()
    var swAppTourChecked = ObservableField<Boolean>(false)

    fun onAppTourCheckChanged(switchCompat: SwitchCompat) {
        if (switchCompat.isPressed) {
            if (switchCompat.isChecked) {
                SharedPreferencesHelper.instance?.putBoolean(SharedPreferencesKeys.APP_TOUR_ENABLED,
                    true)
                SharedPreferencesHelper.instance?.putInteger(SharedPreferencesKeys.APP_TOUR_SKIP_COUNT,
                    0)
            } else {
                SharedPreferencesHelper.instance?.putBoolean(SharedPreferencesKeys.APP_TOUR_ENABLED,
                    false)
                SharedPreferencesHelper.instance?.putInteger(SharedPreferencesKeys.APP_TOUR_SKIP_COUNT,
                    0)
            }
        }
    }

    fun onLanguageClicked() {
        getNavigator()?.openActivity(LanguageUpdateActivity::class.java, null)
    }

    fun onWhatsAppClicked() {
        getNavigator()?.openActivity(WhatsAppOptInOptOutActivity::class.java, null)
    }


    fun onBlockedUsersClicked() {
        getNavigator()?.openActivity(BlockedUsersActivity::class.java, null)
    }

    fun onAppFeatureClicked() {
        getNavigator()?.openWebView(Bundle().apply {
            putString(Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.app_features_url)
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.app_feature))
        })
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

    fun onTermsClick() {
        getNavigator()?.openWebView(Bundle().apply {
            putString(Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.terms_of_service_url)
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.terms_of_service))
        })
    }

    fun onPrivacyClick() {
        getNavigator()?.openWebView(Bundle().apply {
            putString(Constants.PAGE_URL,
                initiateAppDataResponseModel?.gp_api_response_data?.external_link_list?.privacy_policy_url)
            putString(Constants.PAGE_TITLE, getNavigator()?.getMessage(R.string.privacy))
        })
    }

    fun initData() {
        initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )
        swAppTourChecked.set(SharedPreferencesHelper.instance?.getBoolean(
            SharedPreferencesKeys.APP_TOUR_ENABLED))
    }

}