package agstack.gramophone.ui.articles

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityArticlesBinding
import agstack.gramophone.ui.advisory.adapter.ActivityListAdapter
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.*
import androidx.activity.viewModels
import com.amnix.xtension.extensions.isNotNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_articles.*

@AndroidEntryPoint
class ArticlesWebViewActivity :
    BaseActivityWrapper<ActivityArticlesBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null
    private var lastTimeClicked: Long = 0
    private var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        subCategoryViewModel.getBundleData()
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
            viewDataBinding.webView.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    subCategoryViewModel.progress.value = true
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
                    subCategoryViewModel.progress.value = false
                }
            }
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

        }, "Android")
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
        infoClicked: (GpApiResponseData) -> Unit
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
        function: (agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData) -> Unit
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

    override fun enableSortAndFilter() {
        // Don't write anything here. This method is only used in FeaturedActivity & SubCategoryActivity
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

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }


}