package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.ProductImagesFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
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
        /*  Handler().postDelayed({
              initYoutubePlayer()
          }, 500)*/
        initProductDetailView()
        setSelfRatingBarChangeListener()


    }

    private fun setSelfRatingBarChangeListener() {
      viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
          var rating =  rating.toDouble()
          //var ratingselected =   viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.rating.toString()
         mViewModel?.ratingSelected?.set(rating)
          mViewModel?.openAddEditProductReview()


        }

       /* viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.setOnClickListener {
            var ratingselected =   viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.rating.toString()
            println("ratingis"+ratingselected)

        }*/


      /*  viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                var ratingselected = viewDataBinding?.ratingbarReviews?.ratingbarSelfRatingStars?.rating
                println("ratingselected"+ratingselected)
               //uncomment this later
                //mViewModel?.openAddEditProductReview()

            }


            return@OnTouchListener true
        })
*/

    }

    private var genuineCustomerDialog = GenuineCustomerRatingAlertFragment.newInstance()
    override fun showGenuineCustomerRatingDialog(
        genuineCustomerRatingAlertFragment: GenuineCustomerRatingAlertFragment,
        onAddToCartClick: () -> Unit
    ) {
        genuineCustomerDialog = genuineCustomerRatingAlertFragment
        genuineCustomerDialog.setOnClickSelectedListener(onAddToCartClick)
        genuineCustomerDialog.show(supportFragmentManager, "genuineCustomerDialog")
    }


    private fun initProductDetailView() {
        viewDataBinding?.tvShowAllDetails?.setOnClickListener {
            isShowMoreClicked = !isShowMoreClicked

            if (isShowMoreClicked) {
                showMoreOrLessText = resources.getString(R.string.showless)
                drawableEndArrow = getDrawable(R.drawable.ic_arrow_up_orange)!!
            } else {
                showMoreOrLessText = resources.getString(R.string.showmore)
                drawableEndArrow = getDrawable(R.drawable.ic_arrow_down_orange)!!
            }
            viewDataBinding?.tvShowAllDetails?.setText(showMoreOrLessText)
            (viewDataBinding?.tvShowAllDetails as AppCompatTextView).setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawableEndArrow,
                null
            )


            (viewDataBinding?.rvProductDetails?.adapter as ProductDetailsAdapter)
                .isShowMoreSelected = isShowMoreClicked

            (viewDataBinding?.rvProductDetails?.adapter as ProductDetailsAdapter).notifyDataSetChanged()


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
                Log.d("Show cart clicked", "Show Cart")
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
        viewDataBinding?.rvProductSku?.adapter = productSKUAdapter

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
        viewDataBinding.productImagesViewPager?.adapter = productImagesAdapter
        viewDataBinding.dotsIndicator.attachTo(viewDataBinding.productImagesViewPager)
    }

    override fun setProductDetailsAdapter(productDetailsAdapter: ProductDetailsAdapter) {
        viewDataBinding.rvProductDetails?.adapter = productDetailsAdapter
    }

    override fun setRatingAndReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter) {
        viewDataBinding.ratingbarReviews?.rvReviewsProduct?.adapter = ratingAndReviewsAdapter
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
}
