package agstack.gramophone.ui.gramcash.expiringsoon

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GCExpiringSoonViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<GCExpiringSoonNavigator>() {

    var progressLoader = ObservableField<Boolean>(false)
    fun getGramCashTxnExpiringSoon() {

    }
}