package agstack.gramophone.ui.webview.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.login.LoginNavigator
import agstack.gramophone.ui.webview.WebViewNavigator
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import android.text.TextUtils
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
) : BaseViewModel<WebViewNavigator>() {

    var webUrl :String?=""
    var title :String?=""


    fun populateData() {
        var extras = getNavigator()?.getBundle()
        title = extras?.get(Constants.PAGE_TITLE).toString()

        webUrl = extras?.get(Constants.PAGE_URL).toString()

        if (!webUrl?.contains("?")!!) {
            webUrl += "?" + Constants.LANG + "=" + getNavigator()?.getLanguage()
        }
        getNavigator()?.updatePage(webUrl,title)
    }

    @BindingAdapter("loadUrl")
    fun WebView.setUrl(url: String) {
        this.loadUrl(url)
    }


}