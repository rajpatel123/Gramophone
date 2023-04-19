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
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_menu_cart_with_counter.*

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
        shopByViewModel.getStores()

        updateCartCount(SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.CART_ITEM_COUNT)!!)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            shopByViewModel.getStores()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    fun updateCartCount(cartCount: Int) {
        try {
            if (cartCount > 0) {
                tvCartCount!!.text = cartCount.toString()
                frameCartRedCircle!!.visibility = View.VISIBLE
            } else {
                frameCartRedCircle!!.visibility = View.GONE
            }
            rlItemMenuCart.setOnClickListener {
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, "Shop By")
                })
            }
            ivItemMenuCart.setColorFilter(ContextCompat.getColor(this, R.color.blakish), android.graphics.PorterDuff.Mode.SRC_IN)
        } catch (ex: Exception) {
            ex.printStackTrace()
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
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, "Shop By")
                })
            }

        }
        return true
    }

    override fun onClick(view: View?) {
    }

    override fun setShopByCropAdapter(
        shopByCropsAdapter: ShopByCropsAdapter,
        id: (CropData) -> Unit,
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

    override fun onResume() {
        super.onResume()
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