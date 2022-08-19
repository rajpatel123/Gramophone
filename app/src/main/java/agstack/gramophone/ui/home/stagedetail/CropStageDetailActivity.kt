package agstack.gramophone.ui.home.stagedetail


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropStageDetailBinding
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import agstack.gramophone.ui.home.stage.CropStageAdapter
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropStageDetailActivity :
    BaseActivityWrapper<ActivityCropStageDetailBinding, CropStageDetailNavigator, CropStageDetailViewModel>(),
    CropStageDetailNavigator {

    //initialise ViewModel
    private val cropStageDetailNavigator: CropStageDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, "Potato Late Bright", R.drawable.ic_arrow_left)
        cropStageDetailNavigator.setAdapter()
    }

    override fun setRecommendedProductAdapter(
        popularProductAdapter: PopularProductAdapter,
        id: (String) -> Unit,
    ) {
        viewDataBinding.rvRecommendedProduct.adapter = popularProductAdapter
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_crop_stage_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CropStageDetailViewModel {
        return cropStageDetailNavigator
    }

}