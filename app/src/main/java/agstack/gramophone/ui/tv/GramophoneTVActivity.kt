package agstack.gramophone.ui.tv


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityGramophoneTvBinding
import agstack.gramophone.ui.tv.adapter.PlayListAdapter
import agstack.gramophone.ui.tv.adapter.VideoListAdapter
import agstack.gramophone.ui.tv.model.PlayListItemModels
import agstack.gramophone.ui.tv.model.YoutubeChannelItem
import agstack.gramophone.utils.*
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class GramophoneTVActivity :
    BaseActivityWrapper<ActivityGramophoneTvBinding, GramophoneTVNavigator, GramophoneTVViewModel>(),
    GramophoneTVNavigator, YouTubePlayer.OnInitializedListener {

    //initialise ViewModel
    private val gramophoneTVViewModel: GramophoneTVViewModel by viewModels()
    var layoutManager: LinearLayoutManager? = null
    private var nextPageJump = false
    private var nextPageToken: String? = null
    private var nextVideoPageToken: String? = null
    private var sharedvideoId: String? = null
    private var currentPlayingPlayListId: String? = null
    private var currentPlayingPlayListName: String? = null
    private var currentPlayingVideoId: String? = null
    private var currentPlayingVideoDescriptionUrl: String? = null
    private var currentPlayingVideoName: String? = null
    var playListAdapter: PlayListAdapter? = null
    private var videoListAdapter: VideoListAdapter? = null
    private val videoIdsList = ArrayList<String>()
    private val videosTitleHashMap = HashMap<String, PlayListItemModels>()
    private var youTubePlayer: YouTubePlayer? = null
    private var lastTimeClicked: Long = 0
    var position = 0

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private var isLoading: Boolean = false

    // If current page is the last page (Pagination will stop after this page load)
    private var isLastPage: Boolean = false

    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private var isVideoLoading: Boolean = false

    // If current page is the last page (Pagination will stop after this page load)
    private var isVideoLastPage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setToolbarTitle(getString(R.string.tv))
        layoutManager = LinearLayoutManager(this)
        viewDataBinding.showMoreButton.isSelected = false
        viewDataBinding.linearVideoList.visibility = View.GONE
        viewDataBinding.playlistContainer.visibility = View.VISIBLE

        viewDataBinding.showMoreButtonContainer.setOnClickListener(View.OnClickListener {
            if (viewDataBinding.showMoreButton.isSelected) {
                viewDataBinding.showMoreButton.isSelected = false
                viewDataBinding.linearVideoList.visibility = View.GONE
            } else {
                viewDataBinding.showMoreButton.isSelected = true
                viewDataBinding.linearVideoList.visibility = View.VISIBLE
            }
        })

        viewDataBinding.frameHidePlayList.setOnClickListener {
            viewDataBinding.linearVideoList.visibility = View.GONE
        }

        viewDataBinding.frameShare.setOnClickListener {
            if (currentPlayingVideoId != null && videosTitleHashMap.containsKey(
                    currentPlayingVideoId)
            ) {
                val playListItemModels = videosTitleHashMap[currentPlayingVideoId]
                if (playListItemModels != null) {
                    try {
                        val title: String = playListItemModels.snippet.title
                        val imageUrl: String =
                            playListItemModels.snippet.thumbnails.default.url
                        share(title, imageUrl, currentPlayingVideoId!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        viewDataBinding.frameBookmarked.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastTimeClicked > 2000) {
                lastTimeClicked = SystemClock.elapsedRealtime()
                if (currentPlayingVideoId.isNotNullOrEmpty()) {
                    if (viewDataBinding.chkBookmark.isChecked) {
                        viewDataBinding.chkBookmark.isChecked = false
                        gramophoneTVViewModel.bookmarkVideo(currentPlayingVideoId!!, currentPlayingVideoName!!, currentPlayingPlayListName!!, currentPlayingVideoDescriptionUrl!!,false)
                        viewDataBinding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
                    } else {
                        viewDataBinding.chkBookmark.isChecked = true
                        gramophoneTVViewModel.bookmarkVideo(currentPlayingVideoId!!, currentPlayingVideoName!!, currentPlayingPlayListName!!, currentPlayingVideoDescriptionUrl!!, true)
                        viewDataBinding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)
                    }
                }
            }
        }

        gramophoneTVViewModel.getBookmarkedVideoList()
        getPlayLists()
    }

    private fun getPlayLists() {
        val googleApiKey = SharedPreferencesHelper.instance!!.getString(
            SharedPreferencesKeys.GOOGLE_API_KEY)
        googleApiKey?.let {
            gramophoneTVViewModel.getPlayLists("snippet,contentDetails",
                6,
                BuildConfig.CHANNEL_API,
                it
            )
        }
    }

    private fun getPlayListsNextPage(nextPageToken: String) {
        val googleApiKey = SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.GOOGLE_API_KEY)
        if (googleApiKey != null) {
            gramophoneTVViewModel.getPlayListsNextPage("snippet,contentDetails",
                BuildConfig.CHANNEL_API,
                googleApiKey,
                nextPageToken,
                5)
        }
    }

    private fun getVideoList() {
        val googleApiKey = SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.GOOGLE_API_KEY)
        googleApiKey?.let {
            gramophoneTVViewModel.getVideoIds("snippet, contentDetails",
                5,
                currentPlayingPlayListId!!,
                it
            )
        }
    }

    private fun getVideoListNextPage(nextPageToken: String) {
        val googleApiKey = SharedPreferencesHelper.instance!!.getString(
            SharedPreferencesKeys.GOOGLE_API_KEY)
        googleApiKey?.let {
            gramophoneTVViewModel.getVideoIdsNextPage("snippet, contentDetails",
                currentPlayingPlayListId!!,
                it,
                nextPageToken,
                10)
        }
    }

    private fun initPaginationListener() {
        viewDataBinding.playListView.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                if (nextPageToken != null) {
                    getPlayListsNextPage(nextPageToken!!)
                }
                this@GramophoneTVActivity.isLoading = true
            }

            override fun getTotalPageCount(): Int {
                return 0
            }

            override fun isLastPage(): Boolean {
                return this@GramophoneTVActivity.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@GramophoneTVActivity.isLoading
            }
        })
    }

    private fun initVideosPaginationListener(linearLayoutManager: LinearLayoutManager) {
        viewDataBinding.videoListRecyclerView.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                if (nextVideoPageToken != null) {
                    getVideoListNextPage(nextVideoPageToken!!)
                }
                isVideoLoading = true
            }

            override fun getTotalPageCount(): Int {
                return 0
            }

            override fun isLastPage(): Boolean {
                return isVideoLastPage
            }

            override fun isLoading(): Boolean {
                return isVideoLoading
            }
        })
    }

    private fun clearVideosPaginationVariable() {
        videoIdsList.clear()
        position = 0
        isVideoLoading = false
        nextVideoPageToken = null
        isVideoLastPage = false
    }

    private fun initYoutubePlayer() {
        if (youTubePlayer == null) {
            val playerFragment =
                supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragmentX?
            if (playerFragment != null) {
                val googleApiKey = SharedPreferencesHelper.instance!!.getString(
                    SharedPreferencesKeys.GOOGLE_API_KEY)
                playerFragment.initialize(googleApiKey, this)
            }
        } else {
            try {
                youTubePlayer?.cuePlaylist(currentPlayingPlayListId)
                youTubePlayer?.play()
            } catch (e: Exception) {
                e.run { }
            }
        }
    }

    private fun setCurrentPositionInVideoAdapter(videoId: String) {
        position = getCurrentPosition(videoId)
        if (videoListAdapter != null) {
            videoListAdapter!!.setSelectedPosition(position)
        }
    }

    private fun getCurrentPosition(videoId: String): Int {
        for (i in videoIdsList.indices) {
            if (videoId == videoIdsList[i]) {
                return i
            }
        }
        return 0
    }

    private fun share(title: String, imageUrl: String, currentPlayingVideoId: String) {
        val parameterizedUri: Uri = ShareSheetPresenter.BASE_URI.buildUpon()
            .appendQueryParameter(Constants.CategoryKey, Constants.GramophoneVideo)
            .appendQueryParameter(Constants.VideoId, currentPlayingVideoId)
            .appendQueryParameter(Constants.VideoName, currentPlayingVideoName)
            .appendQueryParameter(Constants.PlayListId, currentPlayingPlayListId)
            .appendQueryParameter(Constants.PlayListName, currentPlayingPlayListName).build()


        val extraText =
            title + " " + ShareSheetPresenter.BASE_URI + " \n Check " + imageUrl + " and other video on Gramophone App. Buy best quality agricultural products, get info on weather, mandi price and best advice for better production from Gramophone App."

        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, extraText)
        try {
            startActivity(whatsappIntent)
        } catch (ex: ActivityNotFoundException) {
            showToast(getString(R.string.whatsapp_not_installed))
        }
    }

    private fun isVideoBookmarked() {
        if (gramophoneTVViewModel.bookmarkedList.isNotNullOrEmpty()) {
            var isBookmarked: Boolean = false
            for (item in gramophoneTVViewModel.bookmarkedList) {
                if (item.youtube_video_id == currentPlayingVideoId) {
                    isBookmarked = true
                    break
                }
            }
            if (isBookmarked) {
                viewDataBinding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)
            } else {
                viewDataBinding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
            }
            viewDataBinding.chkBookmark.isChecked = isBookmarked
        }
    }

    override fun populatePlayLists(playLists: List<YoutubeChannelItem>) {
        if (playLists.isNotNullOrEmpty()) {
            if (sharedvideoId == null) {
                val youtubeChannelItem: YoutubeChannelItem = playLists[0]
                currentPlayingPlayListId = youtubeChannelItem.id
                currentPlayingPlayListName = youtubeChannelItem.snippet.title
                getVideoList()
            }
        }
        playListAdapter = PlayListAdapter(applicationContext)
        playListAdapter!!.setCallback(object : PlayListAdapter.Callback {
            override fun onListItemClick(view: View?, position: Int) {
                val playList: YoutubeChannelItem = playListAdapter!!.getItemByPosition(position)
                currentPlayingPlayListId = playList.id
                currentPlayingPlayListName = playList.snippet.title
                getVideoList()
            }
        })
        viewDataBinding.playListView.layoutManager = layoutManager
        viewDataBinding.playListView.adapter = playListAdapter
        playListAdapter!!.setList(playLists)
        initPaginationListener()
    }

    override fun populatePlayListsNextPage(playLists: List<YoutubeChannelItem>) {
        if (playListAdapter != null) {
            playListAdapter!!.removeLoadingFooter()
            playListAdapter!!.addAll(playLists)
            isLoading = false
            if (nextPageToken != null) {
                playListAdapter!!.addLoadingFooter()
            } else isLastPage = true
        }
    }

    override fun setNextPageToken(nextPageToken: String?) {
        this.nextPageToken = nextPageToken
    }

    override fun populateVideosList(videoIds: List<PlayListItemModels>) {
        if (videoIds.isNotNullOrEmpty()) {
            clearVideosPaginationVariable()
            for (i in videoIds.indices) {
                try {
                    val videoId: String = videoIds[i].contentDetails.videoId
                    videoIdsList.add(videoId)
                    videosTitleHashMap[videoId] = videoIds[i]
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            try {
                viewDataBinding.videoListContainer.visibility = View.VISIBLE
                viewDataBinding.titleTextView.text = currentPlayingPlayListName
                viewDataBinding.tvPlayListName.text = currentPlayingPlayListName
                videoListAdapter =
                    VideoListAdapter(applicationContext, videoIds, currentPlayingPlayListName)


                // initYoutubePlayer();
                videoListAdapter!!.setCallback(object : VideoListAdapter.Callback {
                    override fun onListItemClick(view: View?, position: Int) {
                        if (position == RecyclerView.NO_POSITION) {
                            return
                        }
                        val playListItemModels = videoListAdapter!!.getItem(position)
                        val videoId: String =
                            playListItemModels.contentDetails.videoId
                        videoListAdapter!!.setSelectedPosition(position)
                        if (youTubePlayer != null) {
                            try {
                                viewDataBinding.linearVideoList.visibility = View.GONE
                                youTubePlayer?.cuePlaylist(currentPlayingPlayListId)
                                youTubePlayer?.play()
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }
                        }
                        viewDataBinding.videoListRecyclerView.smoothScrollToPosition(position + 1)
                    }
                })

                val linearLayoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                viewDataBinding.videoListRecyclerView.layoutManager = linearLayoutManager
                viewDataBinding.videoListRecyclerView.adapter = videoListAdapter
                videoListAdapter!!.setSelectedPosition(0)
                viewDataBinding.videoListRecyclerView.smoothScrollToPosition(position + 1)
                initVideosPaginationListener(linearLayoutManager)
                initYoutubePlayer()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun populateVideosNextPage(videoIds: List<PlayListItemModels>) {
        if (videoListAdapter != null) {
            for (i in videoIds.indices) {
                try {
                    val videoId: String = videoIds[i].contentDetails.videoId
                    videoIdsList.add(videoId)
                    videosTitleHashMap[videoId] = videoIds[i]
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            videoListAdapter!!.removeLoadingFooter()
            videoListAdapter!!.addAll(videoIds)
            isVideoLoading = false
            if (nextVideoPageToken != null) {
                videoListAdapter!!.addLoadingFooter()
            } else isVideoLastPage = true
        }
    }

    override fun setVideosNextPageToken(nextPageToken: String?) {
        this.nextVideoPageToken = nextPageToken
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        b: Boolean,
    ) {
        this@GramophoneTVActivity.youTubePlayer = youTubePlayer
        youTubePlayer!!.setPlayerStateChangeListener(object :
            YouTubePlayer.PlayerStateChangeListener {
            override fun onLoading() {

            }

            override fun onLoaded(arg0: String?) {
                currentPlayingVideoId = arg0
                isVideoBookmarked()
                setCurrentPositionInVideoAdapter(currentPlayingVideoId!!)
                if (videosTitleHashMap.isNotNullOrEmpty() && videosTitleHashMap.containsKey(
                        currentPlayingVideoId)
                ) {
                    val playListItemModels = videosTitleHashMap[currentPlayingVideoId]
                    try {
                        if (playListItemModels.isNotNull()) {
                            val title: String = playListItemModels!!.snippet.title
                            currentPlayingVideoDescriptionUrl = playListItemModels.snippet.thumbnails.default.url
                            currentPlayingVideoName = title
                            viewDataBinding.playListTitleTextView.text = title
                            viewDataBinding.shareContainer.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onAdStarted() {

            }

            override fun onVideoStarted() {

            }

            override fun onVideoEnded() {

            }

            override fun onError(p0: YouTubePlayer.ErrorReason?) {

            }

        })
        youTubePlayer.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
            override fun onPlaying() {

            }

            override fun onPaused() {

            }

            override fun onStopped() {

            }

            override fun onBuffering(p0: Boolean) {

            }

            override fun onSeekTo(p0: Int) {

            }

        })
        try {
            if (!b) {
                if (videoIdsList.isNotNullOrEmpty()) {
                    try {
                        youTubePlayer.cuePlaylist(currentPlayingPlayListId)
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                youTubePlayer.play()
                            }
                        }, 1000)
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?,
    ) {
        TODO("Not yet implemented")
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left)
    }

    override fun onResume() {
        super.onResume()
        try {
            if (currentPlayingPlayListId != null && nextPageJump) {
                nextPageJump = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_gramophone_tv
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GramophoneTVViewModel {
        return gramophoneTVViewModel
    }

    override fun onBackPressed() {
        if (viewDataBinding.linearVideoList.visibility == View.VISIBLE) {
            viewDataBinding.linearVideoList.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}