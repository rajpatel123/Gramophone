package agstack.gramophone.ui.home.store


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityShopByStoreBinding
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopByStoreAndCropActivity :
    BaseActivityWrapper<ActivityShopByStoreBinding, ShopByStoreNavigator, ShopByStoreViewModel>(),
    ShopByStoreNavigator, View.OnClickListener {

    //initialise ViewModel
    private val shopByStoreViewModel: ShopByStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.shop_by_store), R.drawable.ic_arrow_left)
        shopByStoreViewModel.getBundleData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
    }

    override fun setShopByCropAdapter(shopByCropsAdapter: ShopByCropsAdapter) {
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByCropsAdapter
    }

    override fun setShopByStoresAdapter(shopByStoresAdapter: ShopByStoresAdapter) {
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByStoresAdapter
    }

    override fun setShopByCompanyAdapter(shopByCompanyAdapter: ShopByCompanyAdapter) {
        viewDataBinding.rvShopBy.layoutManager = GridLayoutManager(this, 3)
        viewDataBinding.rvShopBy.setHasFixedSize(true)
        viewDataBinding.rvShopBy.adapter = shopByCompanyAdapter
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

    override fun getViewModel(): ShopByStoreViewModel {
        return shopByStoreViewModel
    }

}