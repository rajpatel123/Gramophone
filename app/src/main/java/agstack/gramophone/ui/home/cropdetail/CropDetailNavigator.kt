package agstack.gramophone.ui.home.cropdetail

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.PopularProductAdapter

interface CropDetailNavigator : BaseNavigator {

    fun setRecommendedProductAdapter(popularProductAdapter: PopularProductAdapter, id: (String) -> Unit)


}
