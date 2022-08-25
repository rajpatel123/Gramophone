package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllTransactionsListViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : BaseViewModel<AllTransactionListNavigator>() {

    var progress = ObservableField<Boolean>(false)


}