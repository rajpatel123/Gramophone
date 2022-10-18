package agstack.gramophone.ui.search.viewmodel


import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GlobalSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<GlobalSearchNavigator>() {
    var progress = MutableLiveData<Boolean>()
    var showEmptyView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        showEmptyView.value = false
    }

    fun onBackPressClick(){
        getNavigator()?.onBackPressClick()
    }

    fun onClearSearchClick(){
        getNavigator()?.onClearSearchClick()
    }

    fun getSuggestions(body: SuggestionsRequest) {
        progress.value = true
        viewModelScope.launch {
            try {
                val response = productRepository.getSuggestions(body)
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val suggestionsResponse = response.body()
                    showEmptyView.value = false
                    getNavigator()?.notifyAdapter(suggestionsResponse?.gp_api_response_data!!)
                } else {
                    showEmptyView.value = true
                    getNavigator()?.notifyAdapter(arrayListOf())
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
                showEmptyView.value = true
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
}