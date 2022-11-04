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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GramophoneTVActivity :
    BaseActivityWrapper<ActivityGramophoneTvBinding, GramophoneTVNavigator, GramophoneTVViewModel>(),
    GramophoneTVNavigator {

    //initialise ViewModel
    private val gramophoneTVViewModel: GramophoneTVViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setToolbarTitle(getString(R.string.tv))
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