package agstack.gramophone.ui.home.stage


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropStageBinding
import agstack.gramophone.ui.home.stagedetail.CropStageDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropStageActivity :
    BaseActivityWrapper<ActivityCropStageBinding, CropStageNavigator, CropStageViewModel>(),
    CropStageNavigator {

    //initialise ViewModel
    private val cropStageViewModel: CropStageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.production_stages), R.drawable.ic_arrow_left)
        cropStageViewModel.setAdapter()
    }

    override fun setCropStageAdapter(cropStageAdapter: CropStageAdapter, id: (String) -> Unit) {
        cropStageAdapter.onItemClicked = id
        viewDataBinding.rvCropStage.adapter = cropStageAdapter
    }

    override fun openCropStageDetailActivity(id: String) {
        openActivity(CropStageDetailActivity::class.java, Bundle().apply {
            putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
        })
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_crop_stage
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CropStageViewModel {
        return cropStageViewModel
    }

}