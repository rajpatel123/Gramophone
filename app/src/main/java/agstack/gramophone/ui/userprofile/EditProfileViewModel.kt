package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<EditProfileNavigator>() {

    var firstName = ObservableField<String>()
    var lastName = ObservableField<String>()
    var mobileNo = ObservableField<String>()
    var OTP = ObservableField<String>()

    fun updateProfile(){

    }

}