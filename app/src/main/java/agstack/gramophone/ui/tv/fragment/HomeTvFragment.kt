package agstack.gramophone.ui.tv.fragment

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseFragment
import agstack.gramophone.databinding.FragmentHomeTvBinding
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import dagger.hilt.android.AndroidEntryPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeTvFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeTvFragment :
    BaseFragment<FragmentHomeTvBinding, HomeTvFragmentNavigator, HomeTvFragmentViewModel>(),
    HomeTvFragmentNavigator, YouTubePlayer.OnInitializedListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var videoId: String? = null

    private val homeTvFragmentViewModel: HomeTvFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * Create Binding
     */
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentHomeTvBinding = FragmentHomeTvBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeYoutube("eboDbvsoMFU")
    }

    private fun initializeYoutube(videoId: String?) {
        if (videoId.isNotNullOrEmpty()) {
            binding?.rlTv?.visibility = View.VISIBLE
            this.videoId = videoId
            initYoutubePlayer()
        } else {
            binding?.rlTv?.visibility = View.GONE
        }
    }

    private fun initYoutubePlayer() {
         /*val playerFragment =
             activity?.supportFragmentManager?.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragmentX?
         if (playerFragment != null) {
             val googleApiKey = getString(R.string.google_api)
             playerFragment.initialize(googleApiKey, this)
         }
        */

        val youtubePlayerFragment = YouTubePlayerFragment()
        youtubePlayerFragment.initialize(getString(R.string.google_api), this)
        val fragmentTransaction: FragmentTransaction = activity?.fragmentManager?.beginTransaction()!!
        fragmentTransaction.replace(R.id.rlTv, youtubePlayerFragment)
        fragmentTransaction.commit()
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean,
    ) {
        if (!wasRestored) {
            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
            try {
                player?.loadVideo(videoId)
            } catch (e: Exception) {
                e.printStackTrace()
                binding?.rlTv?.visibility = View.GONE
            }
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?,
    ) {
        Toast.makeText(
            activity,
            resources.getString(R.string.ensureyoutubeversioninstalled),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_home_tv
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeTvFragmentViewModel {
        return homeTvFragmentViewModel
    }
}