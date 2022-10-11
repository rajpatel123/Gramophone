package agstack.gramophone.ui.orderdetails

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.FreeProduct
import android.os.Bundle

interface OrderDetailsNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (FreeProduct, String) -> Unit,
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun setColorOnOrderStatus(orderStatus: String)

}
