package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.home.adapter.*
import agstack.gramophone.ui.home.view.fragments.market.model.*
import android.os.Bundle

interface MarketFragmentNavigator : BaseNavigator {
    fun setViewPagerAdapter(bannerList: List<Banner>?)

    fun startProductDetailsActivity(it: ProductData)

    fun openSubCategoryActivity(bundle: Bundle)

    fun setHomeAdapter(
        adapter: HomeAdapter,
        onItemClick: (String) -> Unit,
    )

    fun notifyHomeAdapter(
        allBannerResponse: BannerResponse?, categoryResponse: CategoryResponse?,
        allProductsResponse: AllProductsResponse?, cropResponse: CropResponse?,
        storeResponse: StoreResponse?, companyResponse: CompanyResponse?,
        cartList: List<CartItem>?,
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

    fun setExclusiveBannerAdapter(
        adapter: ExclusiveBannerAdapter,
        onItemClick: (String) -> Unit,
    )
}