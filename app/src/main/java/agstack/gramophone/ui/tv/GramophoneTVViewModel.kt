package agstack.gramophone.ui.tv

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.tv.GramophoneTVRepository
import agstack.gramophone.ui.cart.model.PlaceOrderRequest
import agstack.gramophone.ui.tv.model.PlayListItemModels
import agstack.gramophone.ui.tv.model.YoutubeChannelItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class GramophoneTVViewModel @Inject constructor(
    private val gramophoneTVRepository: GramophoneTVRepository,
) : BaseViewModel<GramophoneTVNavigator>() {

    var progress = MutableLiveData<Boolean>()
    val playlistIds: List<YoutubeChannelItem> = ArrayList()
    val videoIds: List<PlayListItemModels> = ArrayList()

    init {
        progress.value = false
    }

    fun getPlayLists(part: String, maxResults: Int, channelId: String, key: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response =
                        gramophoneTVRepository.getPlayLists(part, channelId, key, maxResults)
                    progress.value = false
                    if (response.isSuccessful && response.body()?.items.isNotNullOrEmpty()) {

                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getPlayListsNextPage(
        part: String,
        channelId: String,
        key: String,
        pageToken: String,
        maxResults: Int,
    ) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response = gramophoneTVRepository.getPlayListsNextPage(part,
                        channelId,
                        key,
                        pageToken,
                        maxResults)
                    progress.value = false
                    if (response.isSuccessful && response.body()?.items.isNotNullOrEmpty()) {

                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getVideoIds(part: String, maxResults: Int, playListId: String, key: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response =
                        gramophoneTVRepository.getVideoIds(part, maxResults, playListId, key)
                    progress.value = false
                    if (response.isSuccessful && response.body()?.playListItemModelsList.isNotNullOrEmpty()) {

                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getVideoIdsNextPage(
        part: String,
        playListId: String,
        key: String,
        pageToken: String,
        maxResults: Int,
    ) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response =
                        gramophoneTVRepository.getVideoIdsNextPage(part,
                            maxResults,
                            playListId,
                            key,
                            pageToken)
                    progress.value = false
                    if (response.isSuccessful && response.body()?.playListItemModelsList.isNotNullOrEmpty()) {

                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
}
