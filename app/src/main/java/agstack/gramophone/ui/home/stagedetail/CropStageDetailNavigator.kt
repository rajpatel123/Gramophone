package agstack.gramophone.ui.home.stagedetail

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.PopularProductAdapter

interface CropStageDetailNavigator : BaseNavigator {

    fun setRecommendedProductAdapter(popularProductAdapter: PopularProductAdapter, id: (String) -> Unit)


}
