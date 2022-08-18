package agstack.gramophone.ui.home.stagedetail

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CropStageDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CropStageDetailNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun setAdapter() {
        getNavigator()?.setRecommendedProductAdapter(PopularProductAdapter()) {
//            getNavigator()?.openShopByDetailActivity(it)
        }
    }
}
