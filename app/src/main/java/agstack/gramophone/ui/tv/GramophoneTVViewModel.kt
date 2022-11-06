package agstack.gramophone.ui.tv

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.di.GramophoneTVApiService
import agstack.gramophone.di.RetrofitInstanceForYoutube
import agstack.gramophone.ui.tv.model.YoutubePlayListResponse
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class GramophoneTVViewModel @Inject constructor(
) : BaseViewModel<GramophoneTVNavigator>() {

    var progress = MutableLiveData<Boolean>()


    init {
        progress.value = false
    }

    fun getPlayLists(part: String, maxResults: Int, channelId: String, key: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val service: GramophoneTVApiService =
                        RetrofitInstanceForYoutube.getInstance()!!.create(
                            GramophoneTVApiService::class.java)

                    val call: Call<YoutubePlayListResponse> = service.getPlayLists(part, channelId, key, maxResults)
                    call.enqueue(object : Callback<YoutubePlayListResponse?> {
                        override fun onResponse(
                            call: Call<YoutubePlayListResponse?>,
                            response: Response<YoutubePlayListResponse?>,
                        ) {
                            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                        }

                        override fun onFailure(call: Call<YoutubePlayListResponse?>, t: Throwable) {
                            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                        }
                    })
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
