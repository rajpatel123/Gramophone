package agstack.gramophone.ui.bookmarked

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.tv.GramophoneTVActivity
import agstack.gramophone.ui.tv.SingleVideoActivity
import agstack.gramophone.ui.tv.model.VideoBookMarkedRequest
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
                        getNavigator()?.setBookmarkedAdapter(BookmarkedVideosAdapter(response.body()?.gp_api_response_data?.bookmarks!!),
                            {
                                getNavigator()?.openActivity(
                                    SingleVideoActivity::class.java,
                                    Bundle().apply {
                                        putString(Constants.VideoId, it)
                                    })
                            },
                            {
                                removeFromBookmark(it.youtube_video_id,
                                    it.youtube_video_title,
                                    it.youtube_channel_name, it.youtube_video_desc, false)
                            })
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
        getNavigator()?.openActivity(GramophoneTVActivity::class.java, null)
    }

    fun removeFromBookmark(
        videoId: String,
        videoTitle: String,
        videoChanelName: String,
        videoThumbnail: String,
        isBookmark: Boolean,
    ) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response =
                        productRepository.bookmarkVideo(VideoBookMarkedRequest(videoId,
                            videoTitle,
                            videoThumbnail,
                            "",
                            videoChanelName,
                            isBookmark))
                    progress.value = false
                    if (response.isSuccessful) {
                        getNavigator()?.showToast(response.body()?.gp_api_message)
                        getBookmarkedVideoList()
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
