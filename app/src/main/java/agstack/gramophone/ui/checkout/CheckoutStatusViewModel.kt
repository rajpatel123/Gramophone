package agstack.gramophone.ui.checkout

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.repository.OrderListRepository
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CheckoutStatusViewModel @Inject constructor(
    private val checkoutStatusRepository: CheckoutStatusRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<CheckoutStatusNavigator>() {



}
