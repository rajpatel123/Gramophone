package agstack.gramophone.ui.home.store

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import android.os.Bundle

interface ShopByStoreNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setShopByCropAdapter(
        shopByCropsAdapter: ShopByCropsAdapter,
    )

    fun setShopByStoresAdapter(
        shopByStoresAdapter: ShopByStoresAdapter,
    )

    fun setShopByCompanyAdapter(
        shopByCompanyAdapter: ShopByCompanyAdapter,
    )
}
