package agstack.gramophone.ui.home.shop

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import android.os.Bundle

interface ShopByNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setToolbarTitle(title: String)

    fun setShopByCropAdapter(shopByCropsAdapter: ShopByCropsAdapter, id: (String) -> Unit)

    fun setShopByStoresAdapter(shopByStoresAdapter: ShopByStoresAdapter)

    fun setShopByCompanyAdapter(shopByCompanyAdapter: ShopByCompanyAdapter, id: (String) -> Unit)

    fun openShopByDetailActivity(storeId: String, storeName: String, storeImage: String)

    fun openCropStageActivity(id: String)
}
