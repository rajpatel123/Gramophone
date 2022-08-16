package agstack.gramophone.ui.home.shopbydetail


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityShopByDetailBinding
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ShopByDetailActivity :
    BaseActivityWrapper<ActivityShopByDetailBinding, ShopByDetailNavigator, ShopByDetailViewModel>(),
    ShopByDetailNavigator, View.OnClickListener {

    //initialise ViewModel
    private val shopByDetailViewModel: ShopByDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.pesticides), R.drawable.ic_arrow_left)
        viewDataBinding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            //Check if the view is collapsed
            if (abs(verticalOffset) >= viewDataBinding.appbar.totalScrollRange) {
                viewDataBinding.collapsingToolbar.title = getString(R.string.pesticides)
                viewDataBinding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(
                    this,
                    R.color.blakish))
            } else {
                viewDataBinding.collapsingToolbar.title = ""
            }
        })
        shopByDetailViewModel.setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
    }

    override fun setProductListAdapter(productListAdapter: ProductListAdapter) {
        viewDataBinding.rvProduct.adapter = productListAdapter
        viewDataBinding.nestedScroll.scrollTo(0, 0)
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_shop_by_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ShopByDetailViewModel {
        return shopByDetailViewModel
    }

}