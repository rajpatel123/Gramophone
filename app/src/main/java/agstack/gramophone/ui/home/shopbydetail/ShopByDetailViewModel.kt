package agstack.gramophone.ui.home.shopbydetail

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ShopByDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ShopByDetailNavigator>() {

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

                }
                Constants.SHOP_BY_STORE -> {
                    showWeatherView.value = true

                }
                Constants.SHOP_BY_COMPANY -> {
                    showWeatherView.value = true

                }
            }
        }
    }

    fun setAdapter() {
        getNavigator()?.setProductListAdapter(ProductListAdapter())
    }

}
