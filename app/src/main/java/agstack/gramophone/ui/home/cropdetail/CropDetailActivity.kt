package agstack.gramophone.ui.home.cropdetail


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropStageDetailBinding
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropDetailActivity :
    BaseActivityWrapper<ActivityCropStageDetailBinding, CropDetailNavigator, CropDetailViewModel>(),
    CropDetailNavigator {

    //initialise ViewModel
    private val cropDetailNavigator: CropDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, "Potato", R.drawable.ic_arrow_left)
        cropDetailNavigator.setAdapter()
    }

    override fun setRecommendedProductAdapter(
        popularProductAdapter: PopularProductAdapter,
        id: (String) -> Unit,
    ) {
        viewDataBinding.rvRecommendedProduct.adapter = popularProductAdapter
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_crop_detail
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CropDetailViewModel {
        return cropDetailNavigator
    }

}