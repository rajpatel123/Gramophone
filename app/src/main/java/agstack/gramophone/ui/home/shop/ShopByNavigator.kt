package agstack.gramophone.ui.home.shop

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import android.os.Bundle

interface ShopByNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setToolbarTitle(title: String)

    fun setShopByCropAdapter(shopByCropsAdapter: ShopByCropsAdapter, id: (CropData) -> Unit)

    fun setShopByStoresAdapter(shopByStoresAdapter: ShopByStoresAdapter)

    fun setShopByCompanyAdapter(shopByCompanyAdapter: ShopByCompanyAdapter)

    fun openFeaturedActivityForShopByStore(storeId: String, storeName: String, storeImage: String)

    fun openFeaturedActivityForShopByCompany(companyId: String, companyName: String, companyImage: String)

    fun openCropStageActivity(id: String)
}
