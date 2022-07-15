package agstack.gramophone.ui.verifyotp

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface VerifyOTPNavigator :BaseNavigator {
    fun getBundle(): Bundle?
    fun changeNumber()
    fun showTimer()
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
}