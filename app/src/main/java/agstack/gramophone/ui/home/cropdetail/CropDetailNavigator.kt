package agstack.gramophone.ui.home.cropdetail

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.PopularProductAdapter
import agstack.gramophone.ui.home.stage.CropStageAdapter

interface CropDetailNavigator : BaseNavigator {

    fun setFeaturedProductAdapter(popularProductAdapter: PopularProductAdapter, id: (String) -> Unit)

    fun setCropStageAdapter(cropStageAdapter: CropStageAdapter, id: (String) -> Unit)
}
