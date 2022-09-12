package agstack.gramophone.ui.verifyotp

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import android.os.Bundle

interface VerifyOTPNavigator :BaseNavigator {
    fun getBundle(): Bundle?
    fun showTimer(duration: Long)
    fun onLanguageChangeClick()
    fun getMobileBundle(): Bundle?
    fun updateOTPView()
    fun getInitModel(): InitiateAppDataRequestModel

}