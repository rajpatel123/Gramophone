package agstack.gramophone.ui.home.cropdetail


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropDetailBinding
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.stage.CropStageActivity
import agstack.gramophone.ui.home.stage.CropStageAdapter
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropDetailActivity :
    BaseActivityWrapper<ActivityCropDetailBinding, CropDetailNavigator, CropDetailViewModel>(),
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

        viewDataBinding.flViewAllFeaturedProduct.setOnClickListener {
            openActivity(FeaturedProductActivity::class.java, null)
        }
        viewDataBinding.flViewAllProtectionStage.setOnClickListener {
            openActivity(CropStageActivity::class.java, null)
        }
        viewDataBinding.flViewAllNutritionStage.setOnClickListener {
            openActivity(CropStageActivity::class.java, null)
        }
        viewDataBinding.flViewAllMachineryProduct.setOnClickListener {
            openActivity(FeaturedProductActivity::class.java, null)
        }
    }

    override fun setFeaturedProductAdapter(
        popularProductAdapter: PopularProductAdapter,
        id: (String) -> Unit,
    ) {
        viewDataBinding.rvRecommendedProduct.adapter = popularProductAdapter
        viewDataBinding.rvMachineryProduct.adapter = popularProductAdapter
    }

    override fun setCropStageAdapter(cropStageAdapter: CropStageAdapter, id: (String) -> Unit) {
        viewDataBinding.rvProtectionStage.adapter = cropStageAdapter
        viewDataBinding.rvNutritionStage.adapter = cropStageAdapter
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