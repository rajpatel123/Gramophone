package agstack.gramophone.ui.home.subcategory


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCategoryDetailBinding
import agstack.gramophone.ui.dialog.cart.AddToCartBottomSheetDialog
import agstack.gramophone.ui.dialog.filter.BottomSheetFilterDialog
import agstack.gramophone.ui.dialog.sortby.BottomSheetSortByDialog
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.adapter.ViewPagerAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryViewModel.getBundleData()
        subCategoryViewModel.getBanners()
        setupUi()
    }

    private fun setupUi() {
        viewDataBinding.tvSortBy.setOnClickListener(this)
        viewDataBinding.tvFilter.setOnClickListener(this)
        viewDataBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            //Check if the view is collapsed
            if (abs(verticalOffset) >= viewDataBinding.appbar.totalScrollRange) {
                viewDataBinding.collapsingToolbar.title = getString(R.string.crop_nutritions)
                viewDataBinding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(
                    this,
                    R.color.blakish))
                viewDataBinding.collapsingToolbar.ivToolbar.visibility = View.VISIBLE
            } else {
                viewDataBinding.collapsingToolbar.title = ""
                viewDataBinding.collapsingToolbar.ivToolbar.visibility = View.GONE
            }
        })

        viewDataBinding.dotsIndicator.setOnClickListener { }
        subCategoryViewModel.setAdapter()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvSortBy -> {
                val bottomSheet = BottomSheetSortByDialog()
                bottomSheet.show(
                    supportFragmentManager,
                    "bottomSheet"
                )
            }
            R.id.tvFilter -> {
                val bottomSheet = BottomSheetFilterDialog()
                bottomSheet.show(
                    supportFragmentManager,
                    "bottomSheet"
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun setViewPagerAdapter(bannerList: List<agstack.gramophone.ui.home.view.fragments.market.model.Banner>?) {
        val adapter = ViewPagerAdapter(bannerList!!)
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.dotsIndicator.attachTo(viewDataBinding.viewPager)
    }

    override fun setSubCategoryAdapter(
        subCategoryAdapter: ShopByCategoryAdapter,
        onItemClick: (String) -> Unit,
    ) {
        subCategoryAdapter.itemClicked = onItemClick
        viewDataBinding.rvSubCategory.adapter = subCategoryAdapter
    }

    override fun setProductListAdapter(productListAdapter: ProductListAdapter, onAddToCartClick: (productId: String) -> Unit,) {
        productListAdapter.onAddToCartClick = onAddToCartClick
        viewDataBinding.rvProducts.adapter = productListAdapter
    }

    override fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>
    ) {
        val bottomSheet = AddToCartBottomSheetDialog()
        bottomSheet.mSKUList = mSKUList
        bottomSheet.mSkuOfferList = mSkuOfferList
        bottomSheet.show(
           supportFragmentManager,
            Constants.BOTTOM_SHEET
        )
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