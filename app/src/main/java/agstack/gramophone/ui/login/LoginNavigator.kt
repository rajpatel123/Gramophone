package agstack.gramophone.ui.login

import agstack.gramophone.base.BaseNavigator
import android.content.Intent
import android.os.Bundle

interface LoginNavigator : BaseNavigator{
    fun onLanguageChangeClick()
    fun openWebView(bundle: Bundle)
    fun openReferralDialog()
    fun referralCodeRemoved()
    fun moveToNext(bundle: Bundle)
    fun getBundle(): Bundle?
    fun getMobileBundle(): Bundle?
    fun showMobileNumberHint()
    fun sendLanguageUpdateMoEngageEvent()
    fun sendTermsMoEngageEvent()
    fun sendPrivacyMoEngageEvent()
    fun sendOtpSentMoEngageEvent(mobileNo: String)
    fun scanQR()
}
