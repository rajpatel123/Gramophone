package agstack.gramophone.ui.home.featured

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<FeaturedNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun setAdapter() {
        getNavigator()?.setProductListAdapter(ProductListAdapter())
    }

}
