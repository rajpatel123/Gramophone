package agstack.gramophone.ui.userprofile.firm

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddFirmViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<AddFirmNavigator>() {

    var mobileNo = ObservableField<String>()


    fun SaveandContinue(){

    }

}