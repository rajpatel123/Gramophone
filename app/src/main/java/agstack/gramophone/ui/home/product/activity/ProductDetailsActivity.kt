package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.*
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.widget.MultipleImageDetailDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator, ProductImagesFragment.ProductImagesFragmentInterface,
    YouTubePlayer.OnInitializedListener {


    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private var multipleImageDetailDialog: MultipleImageDetailDialog? = null
    private val TAG = ProductDetailsActivity::class.java.getSimpleName()
    private var isShowMoreClicked: Boolean = false
    private lateinit var showMoreOrLessText: String
    private lateinit var drawableEndArrow: Drawable


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
        var extraText: String?=null
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
        addToCartEnable: Boolean,
        onAddToCartClick: () -> Unit
    ) {
        var genuineCustomerDialog = GenuineCustomerRatingAlertFragment.newInstance(addToCartEnable)
        genuineCustomerDialog = genuineCustomerRatingAlertFragment
        genuineCustomerDialog.setOnClickSelectedListener(onAddToCartClick)
        genuineCustomerDialog.show(supportFragmentManager, "genuineCustomerDialog")
    }

    private var contactforPriceDialog = ContactForPriceBottomSheetDialog.newInstance()
    override fun showContactForPriceBottomSheetDialog(contactForPriceBottomSheetDialog: ContactForPriceBottomSheetDialog) {
        contactforPriceDialog = contactForPriceBottomSheetDialog
        contactforPriceDialog.show(supportFragmentManager, "contactForPriceDialog")
    }


    private var expertAdviceBottomSheet = ExpertAdviceBottomSheetFragment.newInstance()
    override fun showExpertAdviceDialog(
        expertAdviceBottomSheetFragment: ExpertAdviceBottomSheetFragment,
        onOkayClick: () -> Unit,
        onCancelClick: () -> Unit
    ) {
        expertAdviceBottomSheet = expertAdviceBottomSheetFragment
        expertAdviceBottomSheet.setOnClickSelectedListener(onOkayClick, onCancelClick)
        expertAdviceBottomSheet.show(supportFragmentManager, "expertAdviceBottomSheet")
    }

    override fun dismissExpertBottomSheet() {
        expertAdviceBottomSheet.dismiss()
    }


    private fun initProductDetailView() {
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

    }

    private fun initYoutubePlayer() {
        val youTubePlayerFragment = supportFragmentManager
            .findFragmentById(R.id.youtube_player_fragment) as? YouTubePlayerFragment?

        val googleApiKey = instance!!.getString(SharedPreferencesKeys.GOOGLE_API_KEY)

        youTubePlayerFragment?.initialize(googleApiKey, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            try {
                player?.loadVideo("5Eqb_-j3FDA")
                player?.play()
                /* if (videoId != null && !videoId.equals("")) {

                 }*/
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(
            this,
            resources.getString(R.string.ensureyoutubeversioninstalled),
            Toast.LENGTH_LONG
        ).show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.menu_product_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }

        }
        return true
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
        setUpToolBar(true, title!!, R.drawable.ic_arrow_left)
    }

    override fun setProductSKUAdapter(
        productSKUAdapter: ProductSKUAdapter,
        onSKUItemClicked: (ProductSkuListItem) -> Unit
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

    override fun setPercentageOff_mrpVisibility(
        productname:String,
        salesprice: Float,
        discount: String,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactforPriceVisible: Boolean
    ) {
        viewDataBinding.tvProductname.text=productname
        viewDataBinding.tvProductSP.text = resources.getString(R.string.rupee) + "" + salesprice
        if (salesprice.toString().endsWith(".0") || salesprice.toString()
                .contains(".00")
        ) {
            viewDataBinding.tvProductSP.text =
                getString(R.string.rupee) + (salesprice.roundToInt()).toString()
        } else {
            viewDataBinding.tvProductSP.text = getString(R.string.rupee) + (salesprice).toString()
        }


        viewDataBinding.tvPercentageOffOnSelectedSKU.text = discount
        if (isMRPVisible) {
            viewDataBinding.tvProductMRP.visibility = View.VISIBLE
            viewDataBinding.tvPercentageOffOnSelectedSKU.visibility = View.VISIBLE
        } else {
            viewDataBinding.tvProductMRP.visibility = View.GONE
            viewDataBinding.tvPercentageOffOnSelectedSKU.visibility = View.INVISIBLE
        }


        if (isContactforPriceVisible) {
            viewDataBinding.tvContactForPrice.visibility = View.VISIBLE
            viewDataBinding.llPriceDiscount.visibility = View.GONE
            viewDataBinding.llQty.visibility = View.GONE
            viewDataBinding.tvInclusive.visibility = View.GONE
        } else {
            viewDataBinding.tvContactForPrice.visibility = View.GONE
            viewDataBinding.llPriceDiscount.visibility = View.VISIBLE
            viewDataBinding.llQty.visibility = View.VISIBLE
            viewDataBinding.tvInclusive.visibility = View.VISIBLE
        }

        if (!isOffersLayoutVisible) {
            //v2_separator
            viewDataBinding.v2Separator.visibility = View.GONE
            viewDataBinding.rlAvailableOffers.visibility = View.GONE
        } else {
            viewDataBinding.v2Separator.visibility = View.VISIBLE
            viewDataBinding.rlAvailableOffers.visibility = View.VISIBLE
        }

    }

    override fun setProductSKUOfferAdapter(
        productSKUOfferAdapter: ProductSKUOfferAdapter,
        onOfferItemClicked: (PromotionListItem) -> Unit,
        onViewDetailsClicked: (PromotionListItem) -> Unit
    ) {
        productSKUOfferAdapter.selectedProduct = onOfferItemClicked
        productSKUOfferAdapter.onViewAllClicked = onViewDetailsClicked
        viewDataBinding.rvAvailableoffers.adapter = productSKUOfferAdapter
    }


    override fun setRelatedProductsAdapter(
        relatedProductFragmentAdapter: RelatedProductFragmentAdapter,
        onRelatedProductItemClicked: (RelatedProductItem) -> Unit
    ) {

        viewDataBinding.rvRelatedProducts.adapter = relatedProductFragmentAdapter
        relatedProductFragmentAdapter.selectedProduct = onRelatedProductItemClicked

    }

    override fun onItemClick(clickedposition: Int) {
        Log.d("Position of item", clickedposition.toString())
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
        productReviewsData: GpApiResponseData?
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

    override fun updateAddToCartButtonText(newText: String?): String {
        if (newText != null)
            viewDataBinding.tvAddtocart.setText(newText)
        return viewDataBinding.tvAddtocart.text.toString()
    }
}
