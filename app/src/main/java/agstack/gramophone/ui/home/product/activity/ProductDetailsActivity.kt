package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.ProductImagesFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.OffersItem
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.RelatedProductItem
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility.toBulletedList
import agstack.gramophone.widget.MultipleImageDetailDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductDetailsNavigator, ProductDetailsViewModel>(),
    ProductDetailsNavigator , ProductImagesFragment.ProductImagesFragmentInterface,YouTubePlayer.OnInitializedListener {

    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    var productDetailsBulletText = ObservableField<String>()
    private var multipleImageDetailDialog: MultipleImageDetailDialog? = null
    private val TAG= ProductDetailsActivity::class.java.getSimpleName()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productDetailsViewModel.getBundleData()

        productDetailsViewModel.onAddToCartClicked()
        initYoutubePlayer()




    }

    private fun initYoutubePlayer() {
       var youTubePlayerFragment = supportFragmentManager
            .findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerFragment?

        val googleApiKey = instance!!.getString(SharedPreferencesKeys.GOOGLE_API_KEY)


        youTubePlayerFragment?.initialize(googleApiKey,this)
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




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
        onOfferItemClicked: (OffersItem) -> Unit
    ) {
        productSKUOfferAdapter.selectedProduct = onOfferItemClicked
        viewDataBinding?.rvAvailableoffers?.adapter = productSKUOfferAdapter
    }


    override fun setRelatedProductsAdapter(
        relatedProductFragmentAdapter: RelatedProductFragmentAdapter,
        onRelatedProductItemClicked: (RelatedProductItem) -> Unit
    ) {

        viewDataBinding?.rvRelatedProducts?.adapter = relatedProductFragmentAdapter
        relatedProductFragmentAdapter.selectedProduct = onRelatedProductItemClicked

    }

    override fun onItemClick(clickedposition: Int) {
        Log.d("Position of item",clickedposition.toString())
        val allProductImages = mViewModel?.productData?.productImages!! as ArrayList

        multipleImageDetailDialog = MultipleImageDetailDialog.newInstance(allProductImages)
        multipleImageDetailDialog?.show(supportFragmentManager,TAG)

    }

    override fun getFragmentManagerPager(): FragmentManager {
        return supportFragmentManager
    }

    override fun setProductImagesViewPagerAdapter(productImagesAdapter: ProductImagesAdapter) {
        viewDataBinding?.productImagesViewPager?.adapter = productImagesAdapter
        viewDataBinding?.dotsIndicator?.setViewPager(viewDataBinding!!.productImagesViewPager)
    }

    override fun setProductDetailsAdapter(productDetailsAdapter: ProductDetailsAdapter) {
        viewDataBinding?.rvProductDetails?.adapter = productDetailsAdapter
    }


}
