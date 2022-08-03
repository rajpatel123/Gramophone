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

    }

    override fun getLayoutID(): Int = R.layout.activity_settings

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SettingsViewModel = settingsViewModel

    override fun getLanguageCode(): String? = LocaleManagerClass.getLangCodeAsPerAppLocale(this)

    override fun openWebView(bundle: Bundle) {
        openActivity(WebViewActivity::class.java, bundle)
    }

}