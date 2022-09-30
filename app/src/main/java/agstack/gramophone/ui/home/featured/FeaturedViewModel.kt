package agstack.gramophone.ui.home.featured

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<FeaturedNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun getFeaturedProducts() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response =
                        productRepository.getFeaturedProducts(PageLimitRequest("20", "1"))
                    progress.value = false

                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        getNavigator()?.setProductListAdapter(ProductListAdapter(
                            response.body()?.gp_api_response_data?.data),
                            {
//                                fetchProductDetail(it)
                            }, {
                                getNavigator()?.openProductDetailsActivity(ProductData(it))
                            })
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
            }
        }
    }
}
