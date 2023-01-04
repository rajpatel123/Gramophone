package agstack.gramophone.ui.bookmarked

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookmarkedVideosViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<BookmarkedVideosNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var emptyView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        emptyView.value = false
    }

    fun getBookmarkedVideoList() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response = productRepository.getBookmarkedList()
                    progress.value = false
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.bookmarks?.size!! > 0
                    ) {
                        getNavigator()?.setBookmarkedAdapter(BookmarkedVideosAdapter(response.body()?.gp_api_response_data?.bookmarks!!))
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                emptyView.value = true
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun onClickExploreVideos() {

    }
}
