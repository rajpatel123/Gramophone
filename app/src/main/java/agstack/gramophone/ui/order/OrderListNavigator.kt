package agstack.gramophone.ui.order

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import android.os.Bundle

interface OrderListNavigator : BaseNavigator {
    fun setRecentOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String) -> Unit)

    fun setPastOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String) -> Unit)

    fun openOrderDetailsActivity(bundle: Bundle)
}
