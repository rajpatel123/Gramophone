package agstack.gramophone.ui.home.shop


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityShopByStoreBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.cropdetail.CropDetailActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopByActivity :
    BaseActivityWrapper<ActivityShopByStoreBinding, ShopByNavigator, ShopByViewModel>(),
    ShopByNavigator, View.OnClickListener {

    //initialise ViewModel
    private val shopByViewModel: ShopByViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        shopByViewModel.getBundleData()

        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            shopByViewModel.getStores()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search_and_cart, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }

        }
        return true
    }

    override fun onClick(view: View?) {
    }

    override fun setShopByCropAdapter(
        shopByCropsAdapter: ShopByCropsAdapter,
        id: (String) -> Unit,
    ) {
        shopByCropsAdapter.onItemClicked = id
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByCropsAdapter
    }

    override fun setShopByStoresAdapter(
        shopByStoresAdapter: ShopByStoresAdapter,
    ) {
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByStoresAdapter
    }

    override fun setShopByCompanyAdapter(
        shopByCompanyAdapter: ShopByCompanyAdapter,
    ) {
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByCompanyAdapter
    }


    override fun openFeaturedActivityForShopByStore(storeId: String, storeName: String, storeImage: String) {
        openActivity(FeaturedProductActivity::class.java, Bundle().apply {
            putString(Constants.STORE_ID, storeId)
            putString(Constants.STORE_NAME, storeName)
            putString(Constants.STORE_IMAGE, storeImage)
        })
    }

    override fun openFeaturedActivityForShopByCompany(companyId: String, companyName: String, companyImage: String) {
        openActivity(FeaturedProductActivity::class.java, Bundle().apply {
            putString(Constants.COMPANY_ID, companyId)
            putString(Constants.COMPANY_NAME, companyName)
            putString(Constants.COMPANY_IMAGE, companyImage)
        })
    }

    override fun openCropStageActivity(id: String) {
        openActivity(CropDetailActivity::class.java, Bundle().apply {
            putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
        })
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left)
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_shop_by_store
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ShopByViewModel {
        return shopByViewModel
    }

}