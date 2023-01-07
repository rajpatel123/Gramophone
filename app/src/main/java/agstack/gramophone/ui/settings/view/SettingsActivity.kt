package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySettingsBinding
import agstack.gramophone.ui.settings.SettingsNavigator
import agstack.gramophone.ui.settings.viewmodel.SettingsViewModel
import agstack.gramophone.ui.webview.view.WebViewActivity
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity :
    BaseActivityWrapper<ActivitySettingsBinding, SettingsNavigator, SettingsViewModel>(),
    SettingsNavigator {

    private val settingsViewModel: SettingsViewModel by viewModels()
    var isUpdate: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.initData()
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.settings),
            R.drawable.ic_arrow_left
        )
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.setLanguageName()
        if (isUpdate) {
            isUpdate = false
            openAndFinishActivity(SettingsActivity::class.java)
        }

    }

    override fun onPause() {
        super.onPause()
        isUpdate = true
    }

    override fun getLayoutID(): Int = R.layout.activity_settings

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SettingsViewModel = settingsViewModel

    override fun getLanguageCode(): String? = LocaleManagerClass.getLangCodeAsPerAppLocale(this)

    override fun openWebView(bundle: Bundle) {
        openActivity(WebViewActivity::class.java, bundle)
    }


    override fun sendCommonMoEngageEvent(eventName: String) {
        val properties = Properties()
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, eventName, properties)
    }
}