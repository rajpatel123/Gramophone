package agstack.gramophone.ui.search.viewmodel


import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.GlobalSearchResponse
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
open class GlobalSearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val communityRepository: CommunityRepository,
) : BaseViewModel<GlobalSearchNavigator>() {
    var progress = MutableLiveData<Boolean>()
    var lastSearchRequest : GlobalSearchRequest? = null
    var isSearchInCommunity = false

    init {
        progress.value = false
    }

    fun onBackPressClick() {
        getNavigator()?.onBackPressClick()
    }

    fun onClearSearchClick() {
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
                    getNavigator()?.notifySuggestionAdapter(suggestionsResponse?.gp_api_response_data!!)
                } else {
                    getNavigator()?.notifySuggestionAdapter(arrayListOf())
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }


    fun searchByKeyword(body: GlobalSearchRequest, searchInCommunity: Boolean = false) {
        lastSearchRequest = body
        isSearchInCommunity = searchInCommunity

        progress.value = true
        viewModelScope.launch {
            try {
                val response: Response<GlobalSearchResponse> = if (searchInCommunity) {
                    productRepository.searchByKeywordInCommunity(body)
                } else {
                    productRepository.searchByKeyword(body)
                }

                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    val searchResponse = response.body()
                    getNavigator()?.notifySearchResultAdapter(searchResponse?.gp_api_response_data!!.data)
                } else {
                    getNavigator()?.notifySearchResultAdapter(arrayListOf())
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun likePost(body: PostRequestModel) {
        progress.value = true
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.likePost(body)
                    if (response.isSuccessful) {
                        getNavigator()?.onLikePostSuccess(response.body())
                    } else {
                        getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
                    }
                } else {
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                }
                progress.value = false
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }
    }
}