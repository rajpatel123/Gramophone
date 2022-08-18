package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.weather.WeatherRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.adapter.SubCategoryAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<SubCategoryNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun setAdapter() {
        getNavigator()?.setSubCategoryAdapter(SubCategoryAdapter())

        getNavigator()?.setProductListAdapter(ProductListAdapter())
    }

    fun weatherChange() {
        isRainView.value = isRainView.value != true
    }
}
