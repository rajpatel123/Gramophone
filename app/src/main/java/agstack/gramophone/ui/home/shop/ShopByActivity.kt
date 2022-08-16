package agstack.gramophone.ui.home.shop


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityShopByStoreBinding
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.shopbydetail.ShopByDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.Menu
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
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
        id: (String) -> Unit,
    ) {
        shopByStoresAdapter.onItemClicked = id
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByStoresAdapter
    }

    override fun setShopByCompanyAdapter(
        shopByCompanyAdapter: ShopByCompanyAdapter,
        id: (String) -> Unit,
    ) {
        shopByCompanyAdapter.onItemClicked = id
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByCompanyAdapter
    }

    override fun openShopByDetailActivity(id: String) {
        openActivity(ShopByDetailActivity::class.java, Bundle().apply {
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