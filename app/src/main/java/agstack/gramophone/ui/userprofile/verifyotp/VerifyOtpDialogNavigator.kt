package agstack.gramophone.ui.userprofile.verifyotp

import agstack.gramophone.base.BaseNavigator

interface VerifyOtpDialogNavigator :BaseNavigator {
     fun showTimer(duration: Long)
    fun dismissDialogFragment()
}