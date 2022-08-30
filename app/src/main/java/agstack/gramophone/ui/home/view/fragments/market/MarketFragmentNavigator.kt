package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import android.os.Bundle

interface MarketFragmentNavigator : BaseNavigator {
    fun setViewPagerAdapter(bannerList: List<Banner>?)

    fun startProductDetailsActivity(it: ProductData)

    fun openSubCategoryActivity(bundle: Bundle)

    fun setCategoryAdapter(
        adapter: ShopByCategoryAdapter,
        onCategoryClick: (String) -> Unit,
    )

    fun setFeaturedProductsAdapter(
        adapter: ProductListAdapter,
        onProductListItemClick: (ProductData) -> Unit,
    )

    fun setCompanyAdapter(
        adapter: ShopByCompanyAdapter,
        onItemClick: (String) -> Unit,
    )

    fun setStoreAdapter(
        adapter: ShopByStoresAdapter,
        onItemClick: (String) -> Unit,
    )

    fun setCropAdapter(
        adapter: ShopByCropsAdapter,
        onItemClick: (String) -> Unit,
    )
}