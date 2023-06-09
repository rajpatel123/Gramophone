package agstack.gramophone.ui.order

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import android.os.Bundle

interface OrderListNavigator : BaseNavigator {
    fun setPlacedOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String, String) -> Unit)

    fun setRecentOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String, String) -> Unit)

    fun setPastOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String, String) -> Unit)

    fun openOrderDetailsActivity(bundle: Bundle)

    fun openHomeActivity()
}
