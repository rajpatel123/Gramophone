package agstack.gramophone.ui.articles

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.utils.Constants
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
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

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url.equals(pageURL + "articles")) {
                        rlBack.visibility = View.VISIBLE
                    } else {
                        rlBack.visibility = View.GONE
                    }
                    return false
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

        rlBack.setOnClickListener { finish() }
    }
}