package agstack.gramophone.ui.faq

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.referandearn.transaction.AllTransactionsListActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FAQViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<FAQNavigator>() {


}