package agstack.gramophone.ui.home.store

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ShopByStoreViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ShopByStoreNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun setAdapter() {
        showWeatherView.value = true
        getNavigator()?.setShopByStoresAdapter(ShopByStoresAdapter())
    }

    fun weatherChange() {
        isRainView.value = isRainView.value != true
    }
}
