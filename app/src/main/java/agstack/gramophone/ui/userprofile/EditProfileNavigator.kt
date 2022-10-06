package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseNavigator

interface EditProfileNavigator :BaseNavigator {
     fun showVerifyOTPFragment(otpReferenceId: Int, onUpdateSuccess: (String) -> Unit)

}