package agstack.gramophone.ui.home.featured


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityFeaturedProductsBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.SubCategoryNavigator
import agstack.gramophone.ui.home.subcategory.SubCategoryViewModel
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.amnix.xtension.extensions.isNotNull
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class FeaturedProductActivity :
    BaseActivityWrapper<ActivityFeaturedProductsBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    //initialise ViewModel
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        subCategoryViewModel.getBundleData()
    }

    private fun setupUi() {
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            subCategoryViewModel.getFeaturedProducts()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
        viewDataBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        viewDataBinding.toolbar.inflateMenu(R.menu.menu_search_and_cart)
        viewDataBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
        viewDataBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            //Check if the view is collapsed
            if (abs(verticalOffset) >= viewDataBinding.appbar.totalScrollRange) {
                viewDataBinding.collapsingToolbar.title = subCategoryViewModel.toolbarTitle.value
                viewDataBinding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(
                    this,
                    R.color.blakish))
            } else {
                viewDataBinding.collapsingToolbar.title = ""
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }

        }
        return true
    }

    override fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        productListAdapter.onAddToCartClick = onAddToCartClick
        productListAdapter.onProductDetailClick = onProductDetailClick
        viewDataBinding.rvProduct.adapter = productListAdapter
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.PRODUCT, productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {

    }

    override fun setSubCategoryAdapter(subCategoryAdapter: ShopByCategoryAdapter) {
        // No implementation here as there is no subcategory section here on this screen
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<Offer>,
        productData: ProductData,
    ) {
        bottomSheet = AddToCartBottomSheetDialog({
            //Offer detail activity from here
            openActivity(
                OfferDetailActivity::class.java,
                Bundle().apply {

                    val offersDataItem = DataItem()
                    offersDataItem.endDate = it.end_date
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
        })
        bottomSheet?.mSKUList = mSKUList
        bottomSheet?.productData = productData
        bottomSheet?.mSkuOfferList = mSkuOfferList
        bottomSheet?.show(
            supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
    }

    override fun updateOfferApplicabilityOnDialog(isOfferApplicable: Boolean, message: String) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateDialog(isOfferApplicable, message)
    }

    override fun disableSortAndFilter() {
        viewDataBinding.tvSortBy.isEnabled = false
        viewDataBinding.tvFilter.isEnabled = false
    }

    override fun enableSortAndFilter() {
        viewDataBinding.tvSortBy.isEnabled = true
        viewDataBinding.tvFilter.isEnabled = true
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_featured_products
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }
}