package agstack.gramophone.ui.webview.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityTermsOfServiceBinding
import agstack.gramophone.ui.webview.WebViewNavigator
import agstack.gramophone.ui.webview.viewmodel.WebViewViewModel
import agstack.gramophone.utils.LocaleManagerClass
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_terms_of_service.*

@AndroidEntryPoint
class WebViewActivity :
    BaseActivityWrapper<ActivityTermsOfServiceBinding, WebViewNavigator, WebViewViewModel>(),
    WebViewNavigator {
    var progressBar: ProgressBar? = null

    private val webViewViewModel: WebViewViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewContent!!.settings.javaScriptEnabled = true // enable javascript
        progressBar = findViewById<View>(R.id.webViewProgressBar) as ProgressBar?
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        webViewViewModel.populateData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun updatePage(url: String?, title: String?) {
        supportActionBar?.title = title;

        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        webViewContent!!.webViewClient = object : WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                /*if (MySingleton.getInstance().getKbUrl() != null){
                    if (MySingleton.getInstance().getKbUrl().contains(request.getUrl().getAuthority())) {
                        return false;
                    }
            }

                else {
                    Intent intent = new Intent(WebViewActivity.this, UrlHandlerActivity.class);
                    intent.putExtra(IntentKeys.NoClearTaskKey, true);
                    intent.setData(request.getUrl());
                    startActivity(intent);
                    return true;
                }*/
                /*if (MySingleton.getInstance().getKbUrl() != null){
                    if (MySingleton.getInstance().getKbUrl().contains(request.getUrl().getAuthority())) {
                        return false;
                    }
            }

                else {
                    Intent intent = new Intent(WebViewActivity.this, UrlHandlerActivity.class);
                    intent.putExtra(IntentKeys.NoClearTaskKey, true);
                    intent.setData(request.getUrl());
                    startActivity(intent);
                    return true;
                }*/


//                if(Objects.requireNonNull(request.getUrl().getAuthority()).contains("app")) {
////                    Intent webIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
////
////                    // Verify it resolves
////                    PackageManager packageManager = getPackageManager();
////                    List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
////                    boolean isIntentSafe = activities.size() > 0;
////
////                    // Start an activity if it's safe
////                    if (isIntentSafe) {
////                        startActivity(webIntent);
////                    }
//
//                    Intent intent = new Intent(WebViewActivity.this, UrlHandlerActivity.class);
//                    intent.putExtra(IntentKeys.NoClearTaskKey, true);
//                    intent.setData(request.getUrl());
//                    startActivity(intent);
//                    return true;
//                }
                //  return false;
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                progressBar!!.visibility = View.GONE
                loadLocalizedErrorMsg()
            }
        }

        url?.let { webViewContent.loadUrl(it) };
    }


    protected override fun onResume() {
        super.onResume()
    }

    private fun loadLocalizedErrorMsg() {
        val langCode = LocaleManagerClass.getLangCodeFromPreferences(this)
        val completeErrorContentFileName = errorContentFileBaseName + langCode + ".htm"
        val completeFilePath = "file:///android_asset/$completeErrorContentFileName"
        webViewContent!!.loadUrl(completeFilePath)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        private const val errorContentFileBaseName = "offline_message_"
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_terms_of_service
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): WebViewViewModel {
        return webViewViewModel
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }


}