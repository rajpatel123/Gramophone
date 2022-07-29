package agstack.gramophone.ui.orderdetails

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.OfferApplied
import android.os.Bundle

interface OrderDetailsNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (offerList: List<OfferApplied>) -> Unit
    )
}
