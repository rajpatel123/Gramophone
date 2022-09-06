package agstack.gramophone.ui.settings.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLanguageUpdateBinding
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.utils.LocaleManagerClass
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_language.*

@AndroidEntryPoint
class LanguageUpdateActivity :
    BaseActivityWrapper<ActivityLanguageUpdateBinding, LanguageActivityNavigator, LanguageViewModel>(),
    LanguageActivityNavigator {

    private val languageViewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(
            enableBackButton = true,
            getMessage(R.string.language),
            R.drawable.ic_arrow_left
        )
        languageViewModel.populateLanguage()
    }

    override fun getLayoutID() = R.layout.activity_language_update

    override fun getBindingVariable() = BR.viewModel

    override fun getViewModel() = languageViewModel

    override fun moveToNext() {

    }

    override fun updateLanguageList(
        languageAdapter: LanguageAdapter,
        onLanguageClicked: (Language) -> Unit
    ) {
        languageAdapter.selectedLanguage = onLanguageClicked
        rvLanguage.adapter = languageAdapter
    }

    override fun getResource(): Resources = resources

    override fun initiateApp() {
    }

    override fun closeLanguageList() {
        finish()
    }

    override fun onSuccess(message: String?) {
        super.onSuccess(message)
    }

    override fun getLanguageCode(): String? = LocaleManagerClass.getLangCodeAsPerAppLocale(this)

}