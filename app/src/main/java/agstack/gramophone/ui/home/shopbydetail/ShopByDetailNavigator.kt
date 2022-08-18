package agstack.gramophone.ui.home.shopbydetail

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import android.os.Bundle

interface ShopByDetailNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter
    )
}
