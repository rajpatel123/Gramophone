package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySettingsBinding
import agstack.gramophone.ui.settings.SettingsNavigator
import agstack.gramophone.ui.settings.viewmodel.SettingsViewModel
import agstack.gramophone.ui.webview.view.WebViewActivity
import agstack.gramophone.utils.LocaleManagerClass
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity :
    BaseActivityWrapper<ActivitySettingsBinding, SettingsNavigator, SettingsViewModel>(),
    SettingsNavigator {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.settings),
            R.drawable.ic_arrow_left
        )
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.setLanguageName()

    }

    override fun getLayoutID(): Int {
        val activitySettings = R.layout.activity_settings
        return activitySettings
    }

    override fun getBindingVariable(): Int {
        val viewModel = BR.viewModel
        return viewModel
    }

    override fun getViewModel(): SettingsViewModel {
        val settingsViewModel1 = settingsViewModel
        return settingsViewModel1
    }

    override fun getLanguageCode(): String? = LocaleManagerClass.getLangCodeAsPerAppLocale(this)

    override fun openWebView(bundle: Bundle) {
        openActivity(WebViewActivity::class.java, bundle)
    }

}