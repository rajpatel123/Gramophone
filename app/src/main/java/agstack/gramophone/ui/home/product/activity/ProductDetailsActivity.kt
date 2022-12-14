package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.*
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.widget.MultipleImageDetailDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_menu_cart_with_counter.*
import kotlin.math.roundToInt


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator, ProductImagesFragment.ProductImagesFragmentInterface,
    YouTubePlayer.OnInitializedListener {

    private var contactforPriceDialog = ContactForPriceBottomSheetDialog.newInstance()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private var multipleImageDetailDialog: MultipleImageDetailDialog? = null
    private val TAG = ProductDetailsActivity::class.java.getSimpleName()
    private var isShowMoreClicked: Boolean = false
    private lateinit var showMoreOrLessText: String
    private lateinit var drawableEndArrow: Drawable
    var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsViewModel.getBundleData()
        initProductDetailView()
    }

    override fun setRatingBarChangeListener() {
        setSelfRatingBarChangeListener()
    }

    private fun setSelfRatingBarChangeListener() {
        viewDataBinding.ratingbarReviews.ratingbarSelfRatingStars.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                val ratingfinal = rating.toDouble()
                mViewModel?.ratingSelected?.set(ratingfinal)
                mViewModel?.openAddEditProductReview(ratingfinal)
            }
        }
    }

    private fun share() {
        var extraText: String? = null
        if (productDetailsViewModel.productData.get().isNotNull()) {
            extraText =
                productDetailsViewModel.productData.get()?.productBaseName + " " + ShareSheetPresenter.BASE_URI + " \n Check "
            if (productDetailsViewModel.productData.get()?.productImages.isNotNullOrEmpty()) {
                extraText += productDetailsViewModel.productData.get()?.productImages!![0]
            }
            extraText += " and other products on Gramophone App. Buy best quality agricultural products, get info on weather, mandi price and best advice for better production from Gramophone App."

            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, extraText)
            try {
                startActivity(whatsappIntent)
            } catch (ex: ActivityNotFoundException) {
                showToast(getString(R.string.whatsapp_not_installed))
            }
        }
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
                openActivity(CartActivity::class.java)
            }
            ivItemMenuCart?.setColorFilter(ContextCompat.getColor(this, R.color.blakish),
                android.graphics.PorterDuff.Mode.SRC_IN)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun initYoutubePlayer() {
        val playerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragmentX?
        if (playerFragment != null) {
            val googleApiKey = getString(R.string.google_api)
            playerFragment.initialize(googleApiKey, this)
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean,
    ) {
        if (!wasRestored) {
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            try {
                player?.loadVideo(videoId)
                player?.play()
            } catch (e: Exception) {
                e.printStackTrace()
                viewDataBinding.rlHowToUse.visibility = View.GONE
                viewDataBinding.v4Separator.visibility = View.GONE
            }
        }
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
        if (text != null)
            viewDataBinding.tvAddtocart.text = text
        return viewDataBinding.tvAddtocart.text.toString()
    }
}
