package agstack.gramophone.ui.webview

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface WebViewNavigator : BaseNavigator {
    fun getBundle(): Bundle?
    fun updatePage(url: String?, title: String?)
}