package agstack.gramophone.ui.home.store

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter

interface ShopByStoreNavigator : BaseNavigator {

    fun setShopByStoresAdapter(
        shopByStoresAdapter: ShopByStoresAdapter,
    )
}
