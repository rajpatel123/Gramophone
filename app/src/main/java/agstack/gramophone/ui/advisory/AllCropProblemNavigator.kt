package agstack.gramophone.ui.advisory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData

interface AllCropProblemNavigator: BaseNavigator {
    fun setAdvisoryProblemsActivity(cropIssueListAdapter: CropIssueListAdapter, function: (GpApiResponseData) -> Unit?)
}