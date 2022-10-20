package agstack.gramophone.ui.articles

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.amnix.xtension.extensions.isNotNullOrEmpty
import kotlinx.android.synthetic.main.activity_articles.*

class ArticlesWebViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)
        setUp()
    }

    private fun setUp() {
        val webSettings: WebSettings = webView.settings
        val bundle = intent.extras
        if (bundle?.containsKey(Constants.PAGE_URL)!! && bundle.getString(Constants.PAGE_URL) != null) {
            val pageURL = bundle.getString(Constants.PAGE_URL, "")
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.setSupportZoom(false)
            webSettings.defaultTextEncodingName = "utf-8"
            webSettings.loadWithOverviewMode = true
            webSettings.setSupportMultipleWindows(true)
            webSettings.javaScriptCanOpenWindowsAutomatically = false
            webView.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                }

                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean,
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }
            }

            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            if (pageURL.isNotNullOrEmpty())
                webView.loadUrl(pageURL)
        }

        viewBack.setOnClickListener {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        } else {
            finish()
        }
        // If it wasn't the Back key or there's no webpage history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }
}