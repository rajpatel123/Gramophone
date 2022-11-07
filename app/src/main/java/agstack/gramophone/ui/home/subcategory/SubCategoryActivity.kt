package agstack.gramophone.ui.home.subcategory


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCategoryDetailBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.dialog.filter.FilterDialog
import agstack.gramophone.ui.dialog.sortby.BottomSheetSortByDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.adapter.ViewPagerAdapter
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.amnix.xtension.extensions.isNotNull
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_category_detail.view.*
import kotlin.math.abs

@AndroidEntryPoint
class SubCategoryActivity :
    BaseActivityWrapper<ActivityCategoryDetailBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    //initialise ViewModel
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    var bottomSheet: AddToCartBottomSheetDialog? = null
    var sortBy: String = Constants.RELAVENT_CODE
    var subCategoryIdsArray = ArrayList<String>()
    var brandIdsArray = ArrayList<String>()
    var cropIdsArray = ArrayList<String>()
    var technicalIdsArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        subCategoryViewModel.getBundleData()
        subCategoryViewModel.getBanners()
    }

    private fun setupUi() {
        disableSortAndFilter()
        viewDataBinding.tvSortBy.setOnClickListener(this)
        viewDataBinding.tvFilter.setOnClickListener(this)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            subCategoryViewModel.getBundleData()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
        viewDataBinding.toolbar.inflateMenu(R.menu.menu_search_and_cart)
        viewDataBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
        viewDataBinding.toolbar.setNavigationOnClickListener { finish() }
        viewDataBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            //Check if the view is collapsed
            if (abs(verticalOffset) >= viewDataBinding.appbar.totalScrollRange) {
                viewDataBinding.collapsingToolbar.title = subCategoryViewModel.toolbarTitle.value
                viewDataBinding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(
                    this,
                    R.color.blakish))
                viewDataBinding.collapsingToolbar.frameToolbarImage.visibility = View.VISIBLE
            } else {
                viewDataBinding.collapsingToolbar.title = ""
                viewDataBinding.collapsingToolbar.frameToolbarImage.visibility = View.GONE
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvSortBy -> {
                val bottomSheet = BottomSheetSortByDialog(subCategoryViewModel.sortDataList) {
                    sortBy = it
                    subCategoryViewModel.getAllProducts(sortBy,
                        subCategoryIdsArray,
                        brandIdsArray,
                        cropIdsArray,
                        technicalIdsArray,
                        Constants.API_DATA_LIMITS_IN_ONE_TIME,
                        "1")
                }
                bottomSheet.isCancelable = false
                bottomSheet.show(
                    supportFragmentManager,
                    "bottomSheet"
                )
            }
            R.id.tvFilter -> {
                val bottomSheet = FilterDialog(subCategoryViewModel.mainFilterList,
                    subCategoryViewModel.subCategoryList,
                    subCategoryViewModel.brandsList,
                    subCategoryViewModel.cropsList,
                    subCategoryViewModel.technicalDataList) { subCategoryIds, brandIds, cropIds, technicalIds, totalFilterCount ->
                    subCategoryIdsArray = subCategoryIds
                    brandIdsArray = brandIds
                    cropIdsArray = cropIds
                    technicalIdsArray = technicalIds

                    if (totalFilterCount > 0) {
                        viewDataBinding.tvFilterCount.visibility = View.VISIBLE
                        viewDataBinding.tvFilterCount.text = totalFilterCount.toString()
                    } else {
                        viewDataBinding.tvFilterCount.visibility = View.GONE
                        viewDataBinding.tvFilterCount.text = ""
                    }

                    subCategoryViewModel.getAllProducts(sortBy,
                        subCategoryIds,
                        brandIds,
                        cropIds,
                        technicalIds,
                        Constants.API_DATA_LIMITS_IN_ONE_TIME,
                        "1")
                }
                bottomSheet.isCancelable = false
                bottomSheet.show(
                    supportFragmentManager,
                    "bottomSheet"
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }

        }
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        //The Refresh must be only active when the offset is zero :
        viewDataBinding.swipeRefresh.isEnabled = verticalOffset == 0
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.appbar.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        viewDataBinding.appbar.removeOnOffsetChangedListener(this)
    }

    override fun disableSortAndFilter() {
        viewDataBinding.tvSortBy.isEnabled = false
        viewDataBinding.tvFilter.isEnabled = false
    }

    override fun enableSortAndFilter() {
        viewDataBinding.tvSortBy.isEnabled = true
        viewDataBinding.tvFilter.isEnabled = true
    }

    override fun setViewPagerAdapter(bannerList: List<Banner>?) {
        val adapter = ViewPagerAdapter(bannerList!!)
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.dotsIndicator.attachTo(viewDataBinding.viewPager)
    }

    override fun setSubCategoryAdapter(
        subCategoryAdapter: ShopByCategoryAdapter,
    ) {
        viewDataBinding.rvSubCategory.adapter = subCategoryAdapter
    }

    override fun getSubCategoryDetail(
        categoryId: String,
        subCategoryId: String,
        subCategoryName: String,
        subCategoryImage: String,
    ) {
        openActivity(FeaturedProductActivity::class.java, Bundle().apply {
            putString(Constants.SHOP_BY_SUB_CATEGORY, categoryId)
            putString(Constants.SUB_CATEGORY_ID, subCategoryId)
            putString(Constants.SUB_CATEGORY_NAME, subCategoryName)
            putString(Constants.SUB_CATEGORY_IMAGE, subCategoryImage)
        })
    }

    override fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: (productId: Int) -> Unit,
        onProductDetailClick: (productId: Int) -> Unit,
    ) {
        productListAdapter.onAddToCartClick = onAddToCartClick
        productListAdapter.onProductDetailClick = onProductDetailClick
        viewDataBinding.rvProducts.adapter = productListAdapter
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

    override fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean,
        promotionId: String?,
        message: String,
    ) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateDialog(isOfferApplicable, promotionId!!, message)
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.PRODUCT, productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
    }

    override fun loadUrl(url: String) {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun reload() {
        // Don't write anything here. This method is only used in ArticleWebViewActivity
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_category_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }

}