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
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.widget.MultipleImageDetailDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import dagger.hilt.android.AndroidEntryPoint


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
        product_app_name:String,
        salesPrice: String,
        discountPercentage: String,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactforPriceVisible: Boolean
    ) {
        viewDataBinding.tvProductname.text=product_app_name
        viewDataBinding.tvProductSP.setText(resources.getString(R.string.rupee) + "" + salesPrice)
        viewDataBinding.tvPercentageOffOnSelectedSKU.setText(discountPercentage)
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
            viewDataBinding.btnAddtocart.setText(newText)
        return viewDataBinding.btnAddtocart.text.toString()
    }
}
