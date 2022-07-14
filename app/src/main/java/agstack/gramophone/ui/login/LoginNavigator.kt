package agstack.gramophone.ui.login

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface LoginNavigator : BaseNavigator{
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
    fun openWebView(bundle: Bundle)
}
