package agstack.gramophone.ui.home.stage

import agstack.gramophone.base.BaseNavigator

interface CropStageNavigator : BaseNavigator {

    fun setCropStageAdapter(cropStageAdapter: CropStageAdapter, id: (String) -> Unit)

    fun openCropStageDetailActivity(id: String)
}
