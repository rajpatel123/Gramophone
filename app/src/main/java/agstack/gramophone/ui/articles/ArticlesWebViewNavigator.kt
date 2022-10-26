package agstack.gramophone.ui.articles

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface ArticlesWebViewNavigator : BaseNavigator {
    fun getBundle(): Bundle?
    fun loadUrl(url: String)
    fun reload()
}