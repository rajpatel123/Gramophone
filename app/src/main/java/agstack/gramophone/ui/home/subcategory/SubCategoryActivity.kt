package agstack.gramophone.ui.home.subcategory


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCategoryDetailBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.dialog.filter.BottomSheetFilterDialog
import agstack.gramophone.ui.dialog.sortby.BottomSheetSortByDialog
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.adapter.ViewPagerAdapter
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.subcategory.model.GpApiOfferResponse
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
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
    SubCategoryNavigator, View.OnClickListener {

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
                        "10",
                        "1")
                }
                bottomSheet.isCancelable = false
                bottomSheet.show(
                    supportFragmentManager,
                    "bottomSheet"
                )
            }
            R.id.tvFilter -> {
                val bottomSheet = BottomSheetFilterDialog(subCategoryViewModel.mainFilterList,
                    subCategoryViewModel.subCategoryList,
                    subCategoryViewModel.brandsList,
                    subCategoryViewModel.cropsList,
                    subCategoryViewModel.technicalDataList) { subCategoryIds, brandIds, cropIds, technicalIds ->
                    subCategoryIdsArray = subCategoryIds
                    brandIdsArray = brandIds
                    cropIdsArray = cropIds
                    technicalIdsArray = technicalIds

                    subCategoryViewModel.getAllProducts(sortBy,
                        subCategoryIds,
                        brandIds,
                        cropIds,
                        technicalIds,
                        "10",
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

    override fun showCategoryCollapsing() {
        viewDataBinding.rlCategory.visibility = View.VISIBLE
        viewDataBinding.tvStoreName.visibility = View.GONE
    }

    override fun showStoreCollapsing() {
        viewDataBinding.rlCategory.visibility = View.GONE
        viewDataBinding.tvStoreName.visibility = View.VISIBLE
        viewDataBinding.tvStoreName.text = getString(R.string.pesticides)
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
        }, {
            subCategoryViewModel.applyOfferOnProduct(it)
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

    override fun updateAddToCartDialog(
        isShowError: Boolean,
        errorMsg: String,
    ) {
        if (bottomSheet.isNotNull())
            bottomSheet?.updateDialog(isShowError, errorMsg)
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.PRODUCT, productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
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