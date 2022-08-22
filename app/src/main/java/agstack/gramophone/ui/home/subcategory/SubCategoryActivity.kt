package agstack.gramophone.ui.home.subcategory


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCategoryDetailBinding
import agstack.gramophone.ui.dialog.filter.BottomSheetFilterDialog
import agstack.gramophone.ui.dialog.sortby.BottomSheetSortByDialog
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ViewPagerAdapter
import agstack.gramophone.ui.home.model.Banner
import agstack.gramophone.ui.home.subcategory.adapter.SubCategoryAdapter
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
    private lateinit var items: ArrayList<Banner>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        initCards()
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
        val adapter = ViewPagerAdapter(items)
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.dotsIndicator.attachTo(viewDataBinding.viewPager)


        subCategoryViewModel.setAdapter()
    }

    private fun initCards() {
        items = ArrayList<Banner>()

        val cardConnected = Banner(
            R.drawable.dummy_banner,
            getString(R.string.connected),
            getString(R.string.connected_sub_msg)
        )
        items.add(cardConnected)

        val cardDelivery = Banner(
            R.drawable.dummy_banner_2,
            getString(R.string.delivery),
            getString(R.string.delivery_sub_msg)
        )
        items.add(cardDelivery)

        val cardUpdates = Banner(
            R.drawable.dummy_banner,
            getString(R.string.midea),
            getString(R.string.midea_sub_msg)
        )
        items.add(cardUpdates)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
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

    override fun setSubCategoryAdapter(subCategoryAdapter: SubCategoryAdapter) {
        viewDataBinding.rvSubCategory.adapter = subCategoryAdapter
    }

    override fun setProductListAdapter(productListAdapter: ProductListAdapter, onAddToCartClick: (productId: String) -> Unit,) {
        productListAdapter.onAddToCartClick = onAddToCartClick
        viewDataBinding.rvProducts.adapter = productListAdapter
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