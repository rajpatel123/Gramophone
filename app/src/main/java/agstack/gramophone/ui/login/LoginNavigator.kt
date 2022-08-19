package agstack.gramophone.ui.login

import agstack.gramophone.base.BaseNavigator
import android.content.Intent
import android.os.Bundle

interface LoginNavigator : BaseNavigator{
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
    fun openWebView(bundle: Bundle)
    fun openReferralDialog()
    fun referralCodeRemoved()
    fun moveToNext(bundle: Bundle)
    fun getBundle(): Bundle?
    fun getMobileBundle(): Bundle?
    fun showMobileNumberHint()
}
