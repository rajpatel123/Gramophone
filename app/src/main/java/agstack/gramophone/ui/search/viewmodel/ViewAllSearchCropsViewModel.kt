package agstack.gramophone.ui.search.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.search.navigator.ViewAllSearchCropsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewAllSearchCropsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ViewAllSearchCropsNavigator>() {
}