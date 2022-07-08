package agstack.gramophone.ui.order

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter

interface OrderListNavigator : BaseNavigator {
    fun setOrderListAdapter(adapter: OrderListAdapter, onOrderItemClick: () -> Unit)
    fun startOrderDetailsActivity()
}
