package agstack.gramophone.ui.advisory.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCropProblemDetailBinding
import agstack.gramophone.ui.advisory.CropProblemDetailNavigator
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData
import agstack.gramophone.ui.advisory.viewmodel.CropProblemDetailViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropProblemDetailActivity :
    BaseActivityWrapper<ActivityCropProblemDetailBinding, CropProblemDetailNavigator, CropProblemDetailViewModel>(),
    CropProblemDetailNavigator {

    private val cropProblemDetailViewModel: CropProblemDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true,"",R.drawable.ic_action_navigation_arrow_back,true)
        cropProblemDetailViewModel.getRecommendedProduct()
        cropProblemDetailViewModel.setDiseaseDetails()
    }

    override fun getLayoutID(): Int = R.layout.activity_crop_problem_detail

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CropProblemDetailViewModel {
        return cropProblemDetailViewModel
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun setProductList(
        recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter,
        onAddToCartClciked: (GpApiResponseData) -> Unit
    ) {
        recommendedLinkedProductsListAdapter.onAddToCartClicked = onAddToCartClciked
        viewDataBinding.rvRecommendedProduct.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rvRecommendedProduct.setHasFixedSize(true)
        viewDataBinding.rvRecommendedProduct.adapter = recommendedLinkedProductsListAdapter
    }
}