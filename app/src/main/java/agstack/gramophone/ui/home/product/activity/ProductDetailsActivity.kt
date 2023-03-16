package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.*
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import agstack.gramophone.ui.home.view.fragments.market.model.sku.ProductCategory
import agstack.gramophone.utils.*
import agstack.gramophone.widget.MultipleImageDetailDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_menu_cart_with_counter.*
import kotlin.math.roundToInt


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator, ProductImagesFragment.ProductImagesFragmentInterface,
    YouTubePlayer.OnInitializedListener {
    private var productCatId: ArrayList<Int> = ArrayList()
    private var subCatId: ArrayList<Int> = ArrayList()
    private var productCatName: ArrayList<String> = ArrayList()
    private var subCatName: ArrayList<String> = ArrayList()

    private var shareSheetPresenter: ShareHelperClass? = null
    private var productMainImageUrl: String? = null

    private var contactforPriceDialog = ContactForPriceBottomSheetDialog.newInstance()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private var multipleImageDetailDialog: MultipleImageDetailDialog? = null
    private val TAG = ProductDetailsActivity::class.java.getSimpleName()
    private var isShowMoreClicked: Boolean = false
    private lateinit var showMoreOrLessText: String
    private lateinit var drawableEndArrow: Drawable
    var youTubePlayer: YouTubePlayer? = null
    var videoId: String? = null
    var salesPriceAfterDiscount: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setRatingBarChangeListener() {
        setSelfRatingBarChangeListener()
    }

    private fun setSelfRatingBarChangeListener() {
        viewDataBinding.ratingbarReviews.ratingbarSelfRatingStars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                val ratingfinal = rating.toDouble()
                mViewModel?.ratingSelected?.set(ratingfinal)
                sendReviewOpenMoEngageEvent()
                mViewModel?.openAddEditProductReview(ratingfinal)
            }
        }
    }

    private fun share() {
//        var extraText: String? = null
//        if (productDetailsViewModel.productData.get().isNotNull()) {
//            extraText =
//                productDetailsViewModel.productData.get()?.productBaseName + " " + ShareSheetPresenter.BASE_URI + " \n Check "
//            if (productDetailsViewModel.productData.get()?.productImages.isNotNullOrEmpty()) {
//                extraText += productDetailsViewModel.productData.get()?.productImages!![0]
//            }
//            extraText += " and other products on Gramophone App. Buy best quality agricultural products, get info on weather, mandi price and best advice for better production from Gramophone App."
//
//            val whatsappIntent = Intent(Intent.ACTION_SEND)
//            whatsappIntent.type = "text/plain"
//            whatsappIntent.setPackage("com.whatsapp")
//            whatsappIntent.putExtra(Intent.EXTRA_TEXT, extraText)
//            try {
//                startActivity(whatsappIntent)
//            } catch (ex: ActivityNotFoundException) {
//                showToast(getString(R.string.whatsapp_not_installed))
//            }
//        }

        if (shareSheetPresenter == null) {
            val parameterizedUri = ShareSheetPresenter.BASE_URI.buildUpon()
                .appendQueryParameter(ShareKeys.CategoryKey, ShareCategories.ProductDetail)
                .appendQueryParameter(ShareKeys.ProductDetailProductId,
                    productDetailsViewModel.productId.toString()
                ).build()
            val shortUriHandler = object : ShortUriHandler {
                override fun processShortUri(shortUri: Uri) {
                    // Frame text as per short Uri
                    // This shall be used when short link is obtained
                    val extraText = java.lang.String.format(
                        getString(R.string.product_detail_embeded_sharing_msg_short_link),
                        shortUri.toString(),
                        getString(R.string.app_name)
                    )
                    //  shareSheetPresenter.shareDeepLinkWithExtraText(extraText, getString(R.string.product_detail_share_subject));
                    shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
                        extraText,
                        getString(R.string.product_detail_share_subject),
                        IntentKeys.WhatsAppShareKey
                    )
                }
            }
            val genericUriHandler: GenericUriHandler = object : GenericUriHandler {
                override fun processGenericUri(genericUri: Uri) {
                    // Frame text as per long Uri
                    // This shall be used when short link cannot be generated
                    val extraText = java.lang.String.format(
                        getString(R.string.product_detail_embeded_sharing_msg_generic_link),
                        productDetailsViewModel.productData.get()?.productBaseName,
                        getString(R.string.app_name),
                        genericUri.toString()
                    )
                    //shareSheetPresenter.shareDeepLinkWithExtraText(extraText, getString(R.string.product_detail_share_subject));
                    shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
                        extraText,
                        getString(R.string.product_detail_share_subject),
                        IntentKeys.WhatsAppShareKey
                    )
                }
            }
            if (productDetailsViewModel.productData.get()?.productImages?.get(0) != null) {
                //    getImageUri(productMainImageUrl);
                shareSheetPresenter = ShareHelperClass(
                    this,
                    parameterizedUri,
                    ShareAnalyticsSource.androidApp,
                    ShareAnalyticsMedium.social,
                    ShareAnalyticsCampaign.userInitiated,
                    productDetailsViewModel.productData.get()?.productBaseName,
                    "".plus(productDetailsViewModel.productData.get()?.productDetails),
                    Uri.parse(productDetailsViewModel.productData.get()?.productImages?.get(0)),
                    shortUriHandler,
                    genericUriHandler
                )
            } else {
                productMainImageUrl = resources.getDrawable(R.drawable.ic_leaf).toString()
                shareSheetPresenter = ShareHelperClass(
                    this,
                    parameterizedUri,
                    ShareAnalyticsSource.androidApp,
                    ShareAnalyticsMedium.social,
                    ShareAnalyticsCampaign.userInitiated,
                    productDetailsViewModel.productData.get()?.productBaseName,
                    "".plus(productDetailsViewModel.productData.get()?.productDetails),
                    Uri.parse(productMainImageUrl),
                    shortUriHandler,
                    genericUriHandler
                )
            }
        }
        shareSheetPresenter?.shareDynamicLink()


    }

    override fun showGenuineCustomerRatingDialog(
        genuineCustomerRatingAlertFragment: GenuineCustomerRatingAlertFragment,
        addToCartEnabled: Boolean,
        onAddToCartClick: () -> Unit,
    ) {
        var genuineCustomerDialog = GenuineCustomerRatingAlertFragment.newInstance(addToCartEnabled)
        genuineCustomerDialog = genuineCustomerRatingAlertFragment
        genuineCustomerDialog.setOnClickSelectedListener(onAddToCartClick)
        genuineCustomerDialog.show(supportFragmentManager, "genuineCustomerDialog")
    }

    override fun showContactForPriceBottomSheetDialog(contactForPriceBottomSheetDialog: ContactForPriceBottomSheetDialog) {
        contactforPriceDialog = contactForPriceBottomSheetDialog
        contactforPriceDialog.show(supportFragmentManager, "contactForPriceDialog")
    }

    private var expertAdviceBottomSheet = ExpertAdviceBottomSheetFragment.newInstance()
    override fun showExpertAdviceDialog(
        expertAdviceBottomSheetFragment: ExpertAdviceBottomSheetFragment,
        onOkayClick: () -> Unit,
        onCancelClick: () -> Unit,
    ) {
        sendExpertAdviceMoEngageEvent()
        expertAdviceBottomSheet = expertAdviceBottomSheetFragment
        expertAdviceBottomSheet.setOnClickSelectedListener(onOkayClick, onCancelClick)
        expertAdviceBottomSheet.show(supportFragmentManager, "expertAdviceBottomSheet")
    }

    override fun dismissExpertBottomSheet() {
        expertAdviceBottomSheet.dismiss()
    }

    override fun initializeYoutube(videoId: String?) {
        if (videoId.isNotNullOrEmpty()) {
            viewDataBinding.rlHowToUse.visibility = View.VISIBLE
            viewDataBinding.v4Separator.visibility = View.VISIBLE
            this.videoId = videoId
            initYoutubePlayer()
        } else {
            viewDataBinding.rlHowToUse.visibility = View.GONE
            viewDataBinding.v4Separator.visibility = View.GONE
        }
    }

    private fun initProductDetailView() {
        setUpToolBar(true, "", R.drawable.ic_arrow_left)
        viewDataBinding.myToolbar.myToolbar.inflateMenu(R.menu.menu_search_and_cart)
        viewDataBinding.tvShowAllDetails.setOnClickListener {
            isShowMoreClicked = !isShowMoreClicked

            if (isShowMoreClicked) {
                showMoreOrLessText = resources.getString(R.string.showless)
                drawableEndArrow = getDrawable(R.drawable.ic_arrow_up_orange)!!
            } else {
                showMoreOrLessText = resources.getString(R.string.showmore)
                drawableEndArrow = getDrawable(R.drawable.ic_arrow_down_orange)!!
            }
            viewDataBinding.tvShowAllDetails.setText(showMoreOrLessText)
            viewDataBinding.tvShowAllDetails.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawableEndArrow,
                null
            )


            (viewDataBinding.rvProductDetails.adapter as ProductDetailsAdapter)
                .isShowMoreSelected = isShowMoreClicked

            (viewDataBinding.rvProductDetails.adapter as ProductDetailsAdapter).notifyDataSetChanged()


        }

        viewDataBinding.whatsAppShare.setOnClickListener {
            sendShareMoEngageEvent()
            share()
        }
        updateCartCount(SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.CART_ITEM_COUNT)!!)
    }

    override fun updateCartCount(cartCount: Int) {
        try {
            if (cartCount > 0) {
                tvCartCount?.text = cartCount.toString()
                frameCartRedCircle?.visibility = View.VISIBLE
            } else {
                frameCartRedCircle?.visibility = View.GONE
            }
            rlItemMenuCart?.setOnClickListener {
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, "Product Detail")
                })
            }
            ivItemMenuCart?.setColorFilter(ContextCompat.getColor(this, R.color.blakish),
                android.graphics.PorterDuff.Mode.SRC_IN)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun showRating() {
        viewDataBinding.ratingbarReviews.llRateThisProduct.visibility = VISIBLE
        viewDataBinding.ratingbarReviews.llSelfRating.visibility = VISIBLE
    }

    private fun initYoutubePlayer() {
        val playerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragmentX?
        if (playerFragment != null) {
            val googleApiKey = SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.GOOGLE_API_KEY)
            playerFragment.initialize(googleApiKey, this)
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean,
    ) {
        youTubePlayer = player
        if (!wasRestored) {
            youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            try {
                youTubePlayer?.cueVideo(videoId)
            } catch (e: Exception) {
                e.printStackTrace()
                viewDataBinding.rlHowToUse.visibility = View.GONE
                viewDataBinding.v4Separator.visibility = View.GONE
                initYoutubePlayer()
            }
        }

        youTubePlayer!!.setPlayerStateChangeListener(object :
            YouTubePlayer.PlayerStateChangeListener {
            override fun onLoading() {

            }

            override fun onLoaded(arg0: String?) {

            }

            override fun onAdStarted() {

            }

            override fun onVideoStarted() {
                sendWatchVideoMoEngageEvent()
            }

            override fun onVideoEnded() {

            }

            override fun onError(p0: YouTubePlayer.ErrorReason?) {

            }

        })
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?,
    ) {
        Toast.makeText(
            this,
            resources.getString(R.string.ensureyoutubeversioninstalled),
            Toast.LENGTH_LONG
        ).show()
    }


    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.product_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ProductDetailsViewModel {
        return productDetailsViewModel
    }

    override fun setToolbarTitle(title: String?) {
        viewDataBinding.myToolbar.myToolbar.title = title
    }

    override fun setProductSKUAdapter(
        productSKUAdapter: ProductSKUAdapter,
        onSKUItemClicked: (ProductSkuListItem) -> Unit,
    ) {
        productSKUAdapter.selectedProduct = onSKUItemClicked
        viewDataBinding.rvProductSku.adapter = productSKUAdapter

        productSKUAdapter.mSKUList[productSKUAdapter.mSKUList.size-1]?.product_app_name?.let {
            setUpToolBar(true,
                it,R.drawable.ic_arrow_left)

            viewDataBinding.tvProductname.text=it
        }
    }

    override fun refreshSKUAdapter() {
        viewDataBinding.rvProductSku.adapter!!.notifyDataSetChanged()
    }

    override fun refreshOfferAdapter() {
        viewDataBinding.rvAvailableoffers.adapter!!.notifyDataSetChanged()
    }

    override fun updateUIAfterCalculation(
        productName: String,
        salesPrice: Float,
        mrpPrice: Float,
        discount: String,
        isDiscountPercentVisible: Boolean,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactForPriceVisible: Boolean,
    ) {
        viewDataBinding.tvProductname.text = productName
        setSalesPrice(salesPrice)

        if (isMRPVisible) {
            setMrpPrice(mrpPrice)
            viewDataBinding.tvProductMRP.visibility = View.VISIBLE
        } else {
            viewDataBinding.tvProductMRP.visibility = View.GONE
        }

        if (isDiscountPercentVisible) {
            viewDataBinding.tvPercentageOffOnSelectedSKU.text = discount
            viewDataBinding.tvPercentageOffOnSelectedSKU.visibility = View.VISIBLE
        } else {
            viewDataBinding.tvPercentageOffOnSelectedSKU.visibility = View.GONE
        }

        if (isContactForPriceVisible) {
            viewDataBinding.tvContactForPrice.visibility = View.VISIBLE
            viewDataBinding.llPriceDiscount.visibility = View.GONE
            viewDataBinding.llQty.visibility = View.GONE
            viewDataBinding.tvInclusive.visibility = View.GONE
            viewDataBinding.llAddToCart.isEnabled = false
        } else {
            viewDataBinding.tvContactForPrice.visibility = View.GONE
            viewDataBinding.llPriceDiscount.visibility = View.VISIBLE
            viewDataBinding.llQty.visibility = View.VISIBLE
            viewDataBinding.tvInclusive.visibility = View.VISIBLE
            viewDataBinding.llAddToCart.isEnabled = true
        }

        if (!isOffersLayoutVisible) {
            viewDataBinding.v2Separator.visibility = View.GONE
            viewDataBinding.rlAvailableOffers.visibility = View.GONE
        } else {
            viewDataBinding.v2Separator.visibility = View.VISIBLE
            viewDataBinding.rlAvailableOffers.visibility = View.VISIBLE
        }
    }

    private fun setSalesPrice(priceAfterDiscount: Float) {
        salesPriceAfterDiscount = priceAfterDiscount
        if (priceAfterDiscount.toString().endsWith(".0") || priceAfterDiscount.toString()
                .contains(".00")
        ) {
            viewDataBinding.tvProductSP.text =
                getString(R.string.rupee) + priceAfterDiscount.roundToInt().toString()
        } else {
            viewDataBinding.tvProductSP.text =
                getString(R.string.rupee) + priceAfterDiscount.toString()
        }
    }

    private fun setMrpPrice(mrpPrice: Float) {
        if (mrpPrice.toString().endsWith(".0") || mrpPrice.toString()
                .contains(".00")
        ) {
            viewDataBinding.tvProductMRP.text =
                getString(R.string.rupee) + (mrpPrice.roundToInt()).toString()
        } else {
            viewDataBinding.tvProductMRP.text =
                getString(R.string.rupee) + (mrpPrice).toString()
        }
    }

    override fun setProductSKUOfferAdapter(
        productSKUOfferAdapter: AvailableProductOffersAdapter,
        offerListSize: Int,
    ) {
        viewDataBinding.rvAvailableoffers.adapter = productSKUOfferAdapter
        if (offerListSize > 0) {
            viewDataBinding.v2Separator.visibility = View.VISIBLE
            viewDataBinding.rlAvailableOffers.visibility = View.VISIBLE
        } else {
            viewDataBinding.v2Separator.visibility = View.GONE
            viewDataBinding.rlAvailableOffers.visibility = View.GONE
        }
    }

    override fun setRelatedProductsAdapter(
        relatedProductFragmentAdapter: RelatedProductFragmentAdapter,
        relatedProductItemClicked: (RelatedProductItem) -> Unit,
    ) {
        viewDataBinding.rvRelatedProducts.adapter = relatedProductFragmentAdapter
        relatedProductFragmentAdapter.selectedProduct = relatedProductItemClicked
    }

    override fun onItemClick(position: Int) {
        Log.d("Position of item", position.toString())
        val allProductImages = mViewModel?.productData?.get()?.productImages!! as ArrayList

        multipleImageDetailDialog = MultipleImageDetailDialog.newInstance(allProductImages)
        multipleImageDetailDialog?.show(supportFragmentManager, TAG)
    }

    override fun getFragmentManagerPager(): FragmentManager {
        return supportFragmentManager
    }

    override fun setProductImagesViewPagerAdapter(productImagesAdapter: ProductImagesAdapter) {
        viewDataBinding.productImagesViewPager.adapter = productImagesAdapter
        viewDataBinding.dotsIndicator.attachTo(viewDataBinding.productImagesViewPager)
    }

    override fun setProductDetailsAdapter(productDetailsAdapter: ProductDetailsAdapter) {
        viewDataBinding.rvProductDetails.adapter = productDetailsAdapter
    }

    override fun setRatingAndReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter) {
        viewDataBinding.ratingbarReviews.rvReviewsProduct.adapter = ratingAndReviewsAdapter
    }

    override fun openViewAllReviewRatingsActivity(
        productId: Int,
        productReviewsData: GpApiResponseData?,
    ) {
        sendViewAllReviewMoEngageEvent()
        openActivity(ProductAllReviewsActivity::class.java, Bundle().apply {
            putParcelable(Constants.PRODUCTREVIEWDATA, productReviewsData)
            putInt(Constants.PRODUCTID, productId)
        })
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        openActivity(ProductDetailsActivity::class.java, Bundle().apply {
            putParcelable("product", productData)
        })
    }

    override fun updateAddToCartButtonText(text: String?): String {
        if (text != null) {
            viewDataBinding.tvAddtocart.text = text
            sendAddToCartMoEngageEvent()

        }
        return viewDataBinding.tvAddtocart.text.toString()
    }


    override fun onPause() {
        super.onPause()

        try {
            if (youTubePlayer != null)
                youTubePlayer?.pause()
        }catch (ex:Exception){
            initYoutubePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        updateAddToCartButtonText(getMessage(
            R.string.add_to_cart)!!)
        initProductDetailView()

        try {
            if (youTubePlayer != null)
                youTubePlayer?.play()
        }catch (ex:Exception){
            initYoutubePlayer()
        }

        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.IS_GENUENE) == true) {
            productDetailsViewModel.notAGenuineBuyer()
            SharedPreferencesHelper.instance?.putBoolean(SharedPreferencesKeys.IS_GENUENE, false)

        }

        productDetailsViewModel.getBundleData()
    }

    override fun sendProductViewMoEngageEvent(
        productId: Int,
        productBaseName: String,
        avgRating: Double,
        redirectionSource: String,
        customerId: String,
        productCategory: List<ProductCategory>,
    ) {
        val properties = Properties()

        if (productCategory!=null && productCategory.size>0){
            productCategory.forEach {
                productCatId.add(it.category_id)
                productCatName.add(it.category_name)

                if (it.sub_category != null && it.sub_category.size > 0) {
                    productCategory.forEach {
                        subCatId.add(it.category_id)
                        subCatName.add(it.category_name)
                    }
                }



            }

        }




        properties.addAttribute("Product_Id", productId)
            .addAttribute("Product_Category_Id", productCatId.toString())
            .addAttribute("Product_Category_Name", productCatName.toString())
            .addAttribute("Product_Sub_Category_Id", subCatId.toString())
            .addAttribute("Product_Sub_Category_Name", subCatName.toString())
            .addAttribute("Product_Base_Name", productBaseName)
            .addAttribute("Associated_Crop_Id", "")
            .addAttribute("Associated_Farm_Crop_Problem_Id", "")
            .addAttribute("Product_Avg_Rating", avgRating)
            .addAttribute("Redirection_Source", redirectionSource)
            .addAttribute("Customer_Id", customerId)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Product", properties)

        sendViewAllReviewMoEngageEvent()
    }

    override fun sendFavouriteMoEngageEvent(
        productId: Int,
        productBaseName: String,
        customerId: String, isFavourite: Boolean,
    ) {
        val properties = Properties()
        properties.addAttribute("Product_Id", productId)
            .addAttribute("Product_Base_Name", productBaseName)
            .addAttribute("Customer_Id", customerId)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        if (isFavourite) {
            MoEAnalyticsHelper.trackEvent(this, "KA_Add_Product_To_Favorite", properties)
        } else {
            MoEAnalyticsHelper.trackEvent(this, "KA_Remove_Product_From_Favorite", properties)
        }
    }

    private fun sendShareMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName)
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Share_Product", properties)
    }

    private fun sendWatchVideoMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Watch_How_To_Use_Product", properties)
    }

    private fun sendReviewOpenMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Write_Product_Review_Open", properties)
    }

    private fun sendViewAllReviewMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_All_Reviews", properties)
    }

    private fun sendExpertAdviceMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName)
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Get_Expert_Advice", properties)
    }

    private fun sendAddToCartMoEngageEvent() {
        val properties = Properties()


        properties.addAttribute("Product_Id", productDetailsViewModel.productId)
            .addAttribute("Product_Category_Id", productCatId.toString())
            .addAttribute("Product_Category_Name", productCatName.toString())
            .addAttribute("Product_Sub_Category_Id", subCatId.toString())
            .addAttribute("Product_Sub_Category_Name", subCatName.toString())
            .addAttribute(
                "Product_Base_Name",
                productDetailsViewModel.productData.get()?.productBaseName
            )
            .addAttribute("Associated_Crop_Id", "")
            .addAttribute("Associated_Farm_Crop_Problem_Id", "")
            .addAttribute(
                "Product_Avg_Rating",
                ""+productDetailsViewModel.productReviewsData.get()?.rating?.totalRating
            )
            .addAttribute(
                "Krishi_App_Selling_Price",
                ""+productDetailsViewModel.selectedSkuListItem.get()?.salesPrice
            )
            .addAttribute(
                "Product_MRP",
                ""+productDetailsViewModel.selectedSkuListItem.get()?.mrpPrice
            )
            .addAttribute("Selling_Price_After_Discount", salesPriceAfterDiscount)
            .addAttribute(
                "Product_SKU",
                productDetailsViewModel.selectedSkuListItem.get()?.productSku
            )
            .addAttribute("Product_Quantity", productDetailsViewModel.qtySelected.get())
            .addAttribute("Offer_Id", productDetailsViewModel.selectedOfferItem?.promotion_id)
            .addAttribute("Product_Discount_By_Offer",
                productDetailsViewModel.selectedOfferItem?.benefit?.amount_saved)
            .addAttribute("Redirection_Source", "Home")
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Add_Product_To_Cart", properties)
    }
}
