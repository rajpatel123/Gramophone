package agstack.gramophone.ui.settings

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle


interface SettingsNavigator:BaseNavigator {
    fun getLanguageCode(): String?
    fun openWebView(apply: Bundle)
}