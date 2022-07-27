package agstack.gramophone.ui.orderdetails

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<OrderDetailsNavigator>() {

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.ORDER_ID)!! && bundle.getString(Constants.ORDER_ID) != null) {
            getOrderDetails(bundle.get(Constants.ORDER_ID) as String)
        }
    }

    private fun getOrderDetails(orderId: String) {

    }

}
