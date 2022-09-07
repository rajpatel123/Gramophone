package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
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
    var userDatafromIntent = ObservableField<GpApiResponseProfileData>()

    fun updateProfile() {


    }

    fun onUpdateClick() {
        getNavigator()?.showVerifyOTPFragment()
    }

    fun setUserData(userData: GpApiResponseProfileData) {
        userDatafromIntent.set(userData)
        firstName?.set(userDatafromIntent.get()?.first_name)
        lastName?.set(userDatafromIntent.get()?.last_name)
        mobileNo?.set(userDatafromIntent.get()?.mobile_no)
    }

}