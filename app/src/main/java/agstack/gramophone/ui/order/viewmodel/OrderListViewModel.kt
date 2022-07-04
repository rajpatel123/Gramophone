package agstack.gramophone.ui.order.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.repository.OrderListRepository
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderListRepository: OrderListRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<OrderListNavigator>() {



}
