package agstack.gramophone.ui.search.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.farm.navigator.CropGroupExplorerNavigator
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GlobalSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<GlobalSearchNavigator>() {
    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }
}