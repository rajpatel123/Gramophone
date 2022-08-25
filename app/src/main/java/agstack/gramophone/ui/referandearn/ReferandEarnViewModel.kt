package agstack.gramophone.ui.referandearn

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferandEarnViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ReferandEarnNavigator>() {



}