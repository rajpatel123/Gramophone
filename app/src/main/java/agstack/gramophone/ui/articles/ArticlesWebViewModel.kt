package agstack.gramophone.ui.articles

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesWebViewModel @Inject constructor(
) : BaseViewModel<ArticlesWebViewNavigator>() {

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        var webUrl = ""
        if (bundle.isNotNull() && bundle?.containsKey(Constants.PAGE_URL)!! && bundle.getString(
                Constants.PAGE_URL) != null
        ) {
            webUrl = bundle.get(Constants.PAGE_URL).toString()
            if (webUrl.isNotNullOrEmpty() && !webUrl.contains("single-article") && !webUrl.contains("?")) {
                webUrl += "?" + Constants.LANG + "=" + getNavigator()?.getLanguage() + "&" + Constants.GP_TOKEN + "=" + SharedPreferencesHelper.instance?.getString(
                    SharedPreferencesKeys.session_token)!!
            }
        }
        if (webUrl.isNotNullOrEmpty())
            getNavigator()?.loadUrl(webUrl)


        if (webUrl.isNotNullOrEmpty() && bundle.isNotNull() && bundle?.containsKey(Constants.PAGE_SOURCE) == true) {
            viewModelScope.launch {
                delay(1000)
                getNavigator()?.reload()

            }
        }
    }
}
