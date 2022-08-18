package agstack.gramophone.ui.home.cropdetail

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import agstack.gramophone.ui.home.stage.CropStageAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CropDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CropDetailNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun setAdapter() {
        getNavigator()?.setFeaturedProductAdapter(PopularProductAdapter()) {
//            getNavigator()?.openShopByDetailActivity(it)
        }

        getNavigator()?.setCropStageAdapter(CropStageAdapter()) {
//            getNavigator()?.openShopByDetailActivity(it)
        }
    }
}
