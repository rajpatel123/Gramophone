package agstack.gramophone.ui.articles

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityArticlesBinding
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.amnix.xtension.extensions.isNotNull
import kotlinx.android.synthetic.main.activity_articles.*

class ArticlesWebViewActivity :
    BaseActivityWrapper<ActivityArticlesBinding, ArticlesWebViewNavigator, ArticlesWebViewModel>(),
    ArticlesWebViewNavigator {

    private val articlesWebViewModel: ArticlesWebViewModel by viewModels()
    private var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        articlesWebViewModel.getBundleData()
    }

    private fun setupUi() {
        val webSettings: WebSettings = viewDataBinding.webView.settings
        if (webSettings.isNotNull()) {
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
            viewDataBinding.webView.webViewClient = object : WebViewClient() {

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
        }
        viewDataBinding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)

        viewDataBinding.viewBack.setOnClickListener {
            onKeyDown(KeyEvent.KEYCODE_BACK, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
        }

        viewDataBinding.swipeRefresh.viewTreeObserver
            .addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
                viewDataBinding.swipeRefresh.isEnabled = viewDataBinding.webView.scrollY == 0
            }
                .also { mOnScrollChangedListener = it })

        viewDataBinding.swipeRefresh.setOnRefreshListener {
            viewDataBinding.webView.reload()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewDataBinding.webView.canGoBack()) {
            viewDataBinding.webView.goBack()
            return true
        } else {
            finish()
        }
        // If it wasn't the Back key or there's no webpage history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    override fun loadUrl(url: String) {
        viewDataBinding.webView.loadUrl(url)
    }

    override fun reload() {
        viewDataBinding.webView.reload()
    }

    override fun onStop() {
        viewDataBinding.swipeRefresh.viewTreeObserver.removeOnScrollChangedListener(
            mOnScrollChangedListener)
        super.onStop()
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_articles
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ArticlesWebViewModel {
        return articlesWebViewModel
    }


}