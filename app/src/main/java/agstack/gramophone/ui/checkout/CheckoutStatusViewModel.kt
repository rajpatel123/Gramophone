package agstack.gramophone.ui.checkout

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutStatusViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CheckoutStatusNavigator>() {



}
