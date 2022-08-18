package agstack.gramophone.ui.home.stage

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CropStageViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CropStageNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun setAdapter() {
        getNavigator()?.setCropStageAdapter(CropStageAdapter()) {
            getNavigator()?.openCropStageDetailActivity(it)
        }
    }
}
