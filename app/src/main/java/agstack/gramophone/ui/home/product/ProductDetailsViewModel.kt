package agstack.gramophone.ui.home.product

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<ProductDetailsNavigator>() {


fun onAddToCartClicked(){

}
}