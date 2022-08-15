package agstack.gramophone.ui.home.store

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.utils.Constants
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

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.SHOP_BY_TYPE)!! && bundle.getString(Constants.SHOP_BY_TYPE) != null) {
            when (bundle.getString(Constants.SHOP_BY_TYPE)) {
                Constants.SHOP_BY_CROP -> {
                    showWeatherView.value = true
                    getNavigator()?.setShopByCropAdapter(ShopByCropsAdapter())
                }
                Constants.SHOP_BY_STORE -> {
                    showWeatherView.value = true
                    getNavigator()?.setShopByStoresAdapter(ShopByStoresAdapter())
                }
                Constants.SHOP_BY_COMPANY -> {

                }
            }
        }
    }

}
