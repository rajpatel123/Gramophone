package agstack.gramophone.ui.articles

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityArticlesBinding
import agstack.gramophone.ui.advisory.adapter.ActivityListAdapter
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.*
import agstack.gramophone.utils.IntentKeys.WebContentUrlKey
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import com.amnix.xtension.extensions.append
import com.amnix.xtension.extensions.isNotNull
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesWebViewActivity :
    BaseActivityWrapper<ActivityArticlesBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator,ShortUriHandler, GenericUriHandler {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null
    private var lastTimeClicked: Long = 0
    var shareCategory="articles"
    private var summaryEmbeddedTags: String? = null
    private var contentTitles: String? = null
    private var shareSheetPresenter: ShareHelperClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        subCategoryViewModel.getBundleData()
    }

    override fun scrollToActivity(i: Int) {
    }

    override fun updateAddToCartButtonText(message: String) {
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
            viewDataBinding.webView.webChromeClient = MyWebChromeClient()
//            viewDataBinding.webView.webViewClient = object : WebViewClient() {
//
//                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                    super.onPageStarted(view, url, favicon)
//                    subCategoryViewModel.progress.value = true
//                }
//
//                override fun doUpdateVisitedHistory(
//                    view: WebView?,
//                    url: String?,
//                    isReload: Boolean,
//                ) {
//                    super.doUpdateVisitedHistory(view, url, isReload)
//                }
//
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    subCategoryViewModel.progress.value = false
//                }
//            }
        }
        viewDataBinding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        viewDataBinding.webView.addJavascriptInterface(object : JavaScriptInterface() {
            @JavascriptInterface
            override fun goToProduct(productId: String) {
                try {
                    if (SystemClock.elapsedRealtime() - lastTimeClicked < 2000) {
                        return
                    }
                    lastTimeClicked = SystemClock.elapsedRealtime()
                    subCategoryViewModel.fetchProductDetail(productId.toInt())
                    sendViewProductFromArticleMoEngageEvent(productId.toInt())
                    /*val productData = ProductData()
                    productData.product_id = productId.toInt()
                    val bundle = Bundle()
                    bundle.putParcelable(Constants.PRODUCT, productData)
                    openActivity(ProductDetailsActivity::class.java,
                        Bundle().apply {
                            putParcelable(Constants.PRODUCT,
                                productData)
                        })*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            @JavascriptInterface
            override fun openProduct(productId: String) {
                val bundle = Bundle()
                bundle.putParcelable(Constants.PRODUCT, ProductData(productId.toInt()))
                openActivity(ProductDetailsActivity::class.java, bundle)
            }

            @JavascriptInterface
            override fun shareProduct(productId: String) {
                sendShareProductFromArticleMoEngageEvent(productId.toInt())
            }

            @JavascriptInterface
            override fun shareArticle(
                contentUrlString: String,
                contentTitle: String,
                imageUrlString: String,
                summaryEmbeddedTag: String
            ) {
                summaryEmbeddedTags = summaryEmbeddedTag
                contentTitles = contentTitle
                var shareCategoryName= shareCategory.substring(0,1).uppercase()+ shareCategory.substring(1)

                val parameterizedUri = ShareHelperClass.BASE_URI.buildUpon().appendQueryParameter(ShareKeys.CategoryKey, shareCategory).appendQueryParameter(WebContentUrlKey, contentUrlString).appendQueryParameter(IntentKeys.WebContentTitleKey, contentTitle).build()
                shareSheetPresenter = ShareHelperClass(this@ArticlesWebViewActivity,
                    parameterizedUri,
                    ShareAnalyticsSource.androidApp,
                    ShareAnalyticsMedium.social,
                    ShareAnalyticsCampaign.userInitiated,
                    contentTitle,
                    summaryEmbeddedTag,
                    Uri.parse(imageUrlString),
                    this@ArticlesWebViewActivity,
                    this@ArticlesWebViewActivity)
                shareSheetPresenter?.shareDynamicLink()
            }
        }, "Android")

        viewDataBinding.viewBack.setOnClickListener {
            onKeyDown(KeyEvent.KEYCODE_BACK, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
        }
    }

    private class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult,
        ): Boolean {
            return true
        }

        override fun onJsConfirm(
            view: WebView,
            url: String,
            message: String,
            result: JsResult,
        ): Boolean {
            return true
        }

        override fun onJsPrompt(
            view: WebView, url: String, message: String, defaultValue: String,
            result: JsPromptResult,
        ): Boolean {
            return true
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

    override fun setAdvisoryActivity(
        activityListAdapter: ActivityListAdapter,
        function: (GpApiResponseData) -> Unit,
        infoClicked: (GpApiResponseData) -> Unit,
        position: (Int) -> Unit

    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity

    }


    override fun updateActivitiesList(it: GpApiResponseData) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity

    }

    override fun setAdvisoryProblemsActivity(
        activityListAdapter: CropIssueListAdapter,
        function: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit
    ) {
        TODO("Not yet implemented")
    }



    override fun showInfoBottomSheet() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity

    }

    override fun openIssueImagesBottomSheet(it: GpApiResponseData) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity

    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun setSubCategoryAdapter(subCategoryAdapter: ShopByCategoryAdapter) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun getSubCategoryDetail(
        categoryId: String,
        subCategoryId: String,
        subCategoryName: String,
        subCategoryImage: String,
    ) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>,
        productData: ProductData,
    ) {
        bottomSheet = AddToCartBottomSheetDialog({
            //Offer detail activity from here
            openActivity(
                OfferDetailActivity::class.java,
                Bundle().apply {

                    val offersDataItem = DataItem()
                    offersDataItem.endDate = it.valid_till
                    offersDataItem.productName = it.title
                    offersDataItem.productsku = it.applicable_on_sku
                    offersDataItem.image = it.image
                    offersDataItem.termsConditions = it.t_c
                    putParcelable(Constants.OFFERSDATA, offersDataItem)

                })
        }, {
            subCategoryViewModel.checkOfferApplicability(it)
        }, {
            subCategoryViewModel.onAddToCartClicked(it)
        }, { productId, quantity ->
            subCategoryViewModel.loadOffersData(productId, quantity, true)
        })
        bottomSheet?.mSKUList = mSKUList
        bottomSheet?.productData = productData
        bottomSheet?.mSkuOfferList = mSkuOfferList
        bottomSheet?.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean,
        promotionId: String?,
        message: String,
    ) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateDialog(isOfferApplicable, promotionId!!, message)
    }

    override fun updateOfferOnAddToCartDialog(mSkuOfferList: ArrayList<PromotionListItem?>) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateOffer(mSkuOfferList)
    }

    override fun disableSortAndFilter() {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun disableFilterOnly() {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun enableSortAndFilter() {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
    }

    override fun updateCartCount(cartCount: Int) {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
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

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }

    private fun sendViewProductFromArticleMoEngageEvent(productId: Int) {
        val properties = Properties()
            .addAttribute("Product_Id", productId)
            .addAttribute("Redirection_Source", "ArticleWebView")
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Product", properties)
    }

    private fun sendShareProductFromArticleMoEngageEvent(productId: Int) {
        val properties = Properties()
            .addAttribute("Source screen", "Article")

            .addAttribute("Product_Id", productId)
            .addAttribute("Redirection_Source", "ArticleWebView")
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Share_Article", properties)
    }

    override fun processShortUri(shortUri: Uri) {
        val completeString = summaryEmbeddedTags?.append(shortUri.toString())
        if (completeString != null && contentTitles != null) {
            shareSheetPresenter!!.shareDeepLinkWithExtraText(completeString, contentTitles!!)
        }
    }

    override fun processGenericUri(genericUri: Uri) {
        // Frame text as per short Uri
        // This shall be used when short link is obtained
        val completeString = summaryEmbeddedTags?.replace(CONTENT_URL_TAG, genericUri.toString())
        if (completeString != null && contentTitles != null) {
            shareSheetPresenter?.shareDeepLinkWithExtraText(completeString, contentTitles!!)
        }
    }

    companion object {
        private val CONTENT_URL_TAG = "_content_url"
        private val GENERIC_IMAGE_URI = Uri.parse("http://res.cloudinary.com/agstack/image/upload/v1/web/logo.png")

    }
}