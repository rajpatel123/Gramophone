package agstack.gramophone.ui.tv

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.di.GramophoneTVApiService
import agstack.gramophone.di.RetrofitInstanceForYoutube
import agstack.gramophone.ui.tv.model.ItemsModelResponse
import agstack.gramophone.ui.tv.model.PlayListItemModels
import agstack.gramophone.ui.tv.model.YoutubeChannelItem
import agstack.gramophone.ui.tv.model.YoutubeModel
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
    val videoIds: ArrayList<PlayListItemModels> = ArrayList()

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

                    val call: Call<YoutubeModel> = service.getPlayLists(part, channelId, key, maxResults)
                    call.enqueue(object : Callback<YoutubeModel?> {
                        override fun onResponse(
                            call: Call<YoutubeModel?>,
                            response: Response<YoutubeModel?>,
                        ) {
                            val playlistIds = ArrayList<YoutubeChannelItem>()
                            val youtubeModel: YoutubeModel = response.body()!!
                            for (i in 0 until youtubeModel.items.size) {
                                val playlist: YoutubeChannelItem = youtubeModel.items.get(i)
                                playlistIds.add(playlist)
                            }
                            getNavigator()?.populatePlayLists(playlistIds)
                            getNavigator()?.setNextPageToken(youtubeModel.nextPageToken)
                        }

                        override fun onFailure(call: Call<YoutubeModel?>, t: Throwable) {
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

                    val service: GramophoneTVApiService =
                        RetrofitInstanceForYoutube.getInstance()!!.create(
                            GramophoneTVApiService::class.java)

                    val call: Call<YoutubeModel> = service.getPlayListsNextPage(part, channelId, key, pageToken, maxResults)
                    call.enqueue(object : Callback<YoutubeModel?> {
                        override fun onResponse(
                            call: Call<YoutubeModel?>,
                            response: Response<YoutubeModel?>,
                        ) {
                            val playlistIds = ArrayList<YoutubeChannelItem>()
                            val youtubeModel: YoutubeModel = response.body()!!
                            for (i in 0 until youtubeModel.items.size) {
                                val playlist: YoutubeChannelItem = youtubeModel.items[i]
                                playlistIds.add(playlist)
                            }
                            getNavigator()?.populatePlayListsNextPage(playlistIds)
                            getNavigator()?.setNextPageToken(youtubeModel.nextPageToken)
                        }

                        override fun onFailure(call: Call<YoutubeModel?>, t: Throwable) {
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

    fun getVideoIds(part: String, maxResults: Int, playListId: String, key: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val service: GramophoneTVApiService =
                        RetrofitInstanceForYoutube.getInstance()!!.create(
                            GramophoneTVApiService::class.java)

                    val call: Call<ItemsModelResponse> = service.getVideoIds(part, maxResults, playListId, key)
                    call.enqueue(object : Callback<ItemsModelResponse?> {
                        override fun onResponse(
                            call: Call<ItemsModelResponse?>,
                            response: Response<ItemsModelResponse?>,
                        ) {
                            videoIds.clear()
                            videoIds.addAll(response.body()!!.items)
                            getNavigator()?.populateVideosList(videoIds)
                            getNavigator()?.setVideosNextPageToken(response.body()!!.nextPageToken)
                        }

                        override fun onFailure(call: Call<ItemsModelResponse?>, t: Throwable) {
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

                    val service: GramophoneTVApiService =
                        RetrofitInstanceForYoutube.getInstance()!!.create(
                            GramophoneTVApiService::class.java)

                    val call: Call<ItemsModelResponse> = service.getVideoIdsNextPage(part, playListId, key, pageToken, maxResults)
                    call.enqueue(object : Callback<ItemsModelResponse?> {
                        override fun onResponse(
                            call: Call<ItemsModelResponse?>,
                            response: Response<ItemsModelResponse?>,
                        ) {
                            val videoIds: ArrayList<PlayListItemModels> = ArrayList()
                            val itemsModelResponse: ItemsModelResponse = response.body()!!
                            for (i in 0 until itemsModelResponse.items.size) {
                                val videoId: PlayListItemModels = itemsModelResponse.items[i]
                                videoIds.add(videoId)
                            }
                            getNavigator()?.populateVideosNextPage(videoIds)
                            getNavigator()?.setVideosNextPageToken(response.body()!!.nextPageToken)
                        }

                        override fun onFailure(call: Call<ItemsModelResponse?>, t: Throwable) {
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
}
