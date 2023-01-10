package agstack.gramophone.ui.tv

import agstack.gramophone.R
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX


class SingleVideoActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {
    private var youTubePlayer: YouTubePlayer? = null
    private var videoId: String? = null
    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean,
    ) {
        this.youTubePlayer = p1;
        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)
        /* Start buffering **/
        try {
            if (!p2) {
                youTubePlayer?.loadVideo(videoId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?,
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_video)
        videoId = intent.getStringExtra(Constants.VideoId);
        val playerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragmentX?


        val googleApiKey =
            SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.GOOGLE_API_KEY)


        if (googleApiKey != null) {
            playerFragment?.initialize(googleApiKey, this)
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onBuffering(arg0: Boolean) {


        }

        override fun onPaused() {

        }

        override fun onPlaying() {

        }

        override fun onSeekTo(arg0: Int) {

        }

        override fun onStopped() {

            //  Log.i(TAG,"onStopped");

        }
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {

        }

        override fun onError(arg0: YouTubePlayer.ErrorReason) {

        }

        override fun onLoaded(arg0: String) {

        }

        override fun onLoading() {

        }

        override fun onVideoEnded() {


        }

        override fun onVideoStarted() {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayer?.release()

    }
}
