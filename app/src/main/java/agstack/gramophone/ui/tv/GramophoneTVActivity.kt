package agstack.gramophone.ui.tv


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityGramophoneTvBinding
import agstack.gramophone.databinding.ActivityWeatherBinding
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.utils.Constants
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GramophoneTVActivity :
    BaseActivityWrapper<ActivityGramophoneTvBinding, GramophoneTVNavigator, GramophoneTVViewModel>(),
    GramophoneTVNavigator {

    //initialise ViewModel
    private val gramophoneTVViewModel: GramophoneTVViewModel by viewModels()
    var layoutManager: LinearLayoutManager? = null
    private val currentPlayingPlayListId: String? = null
    private var nextPageJump = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setToolbarTitle(getString(R.string.tv))
        layoutManager = LinearLayoutManager(this)
        viewDataBinding.showMoreButton.isSelected = false
        viewDataBinding.videoListRecyclerView.visibility = View.GONE
        viewDataBinding.showMoreIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_down))
        viewDataBinding.playlistContainer.visibility = View.VISIBLE

        viewDataBinding.showMoreButtonContainer.setOnClickListener(View.OnClickListener {
            if (viewDataBinding.showMoreButton.isSelected) {
                viewDataBinding.showMoreButton.isSelected = false
                viewDataBinding.videoListRecyclerView.setVisibility(View.GONE)
                viewDataBinding.showMoreIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_down))
            } else {
                viewDataBinding.showMoreButton.isSelected = true
                viewDataBinding.videoListRecyclerView.visibility = View.VISIBLE
                viewDataBinding.showMoreIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_up))
            }
        })

        getPlayLists()
    }

    private fun getPlayLists() {
        val googleApiKey = getString(R.string.google_api)
        gramophoneTVViewModel.getPlayLists("snippet,contentDetails",
            6,
            getString(R.string.channel_id),
            googleApiKey)
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left)
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

}