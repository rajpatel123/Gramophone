package agstack.gramophone.ui.order.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.view.fragments.market.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.repository.OrderListRepository
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderListRepository: OrderListRepository,
) : BaseViewModel<OrderListNavigator>() {

    suspend fun getOrderList(hashMap: HashMap<Any, Any>) {
        withContext(
            Dispatchers.IO
        ) {

            //set received orders list in Adapter
            getNavigator()?.setOrderListAdapter(OrderListAdapter()) {


                getNavigator()?.startOrderDetailsActivity()

            }


        }
    }

}
