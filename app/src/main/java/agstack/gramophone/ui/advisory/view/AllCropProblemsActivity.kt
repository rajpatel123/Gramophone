package agstack.gramophone.ui.advisory.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityAllCropProblemsBinding
import agstack.gramophone.ui.advisory.AllCropProblemNavigator
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData
import agstack.gramophone.ui.advisory.viewmodel.CropProblemViewModel
import agstack.gramophone.utils.Constants.CROP_ID
import agstack.gramophone.utils.Constants.STAGE_ID
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_advisory.*

@AndroidEntryPoint
class AllCropProblemsActivity : BaseActivityWrapper<ActivityAllCropProblemsBinding, AllCropProblemNavigator,CropProblemViewModel>(), AllCropProblemNavigator {

   private val cropProblemViewModel : CropProblemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true,getString(R.string.crop_all_problems),R.drawable.ic_action_navigation_arrow_back,false)
        cropProblemViewModel.getCropProblemList(intent.extras?.getInt(STAGE_ID),intent.extras?.getInt(CROP_ID))
    }

    override fun getLayoutID(): Int = R.layout.activity_all_crop_problems

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): CropProblemViewModel {
        return cropProblemViewModel
    }

    override fun setAdvisoryProblemsActivity(
        cropIssueListAdapter: CropIssueListAdapter,
        onProblemSelected: (GpApiResponseData) -> Unit?
    ) {
        cropIssueListAdapter.onProblemSelected = onProblemSelected
        viewDataBinding.rvCropProblems.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.rvCropProblems.setHasFixedSize(true)
        viewDataBinding.rvCropProblems.adapter = cropIssueListAdapter
    }
}