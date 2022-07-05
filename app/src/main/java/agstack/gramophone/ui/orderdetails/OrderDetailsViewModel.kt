package agstack.gramophone.ui.orderdetails

import agstack.gramophone.base.BaseViewModel
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderDetailsRepository: OrderDetailsRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<OrderDetailsNavigator>() {



}
