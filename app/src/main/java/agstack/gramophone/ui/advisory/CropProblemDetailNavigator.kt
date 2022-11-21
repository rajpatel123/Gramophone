package agstack.gramophone.ui.advisory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.recomondedproducts.GpApiResponseData
import android.os.Bundle

interface CropProblemDetailNavigator :BaseNavigator {
    fun getBundle(): Bundle?
    fun setProductList(recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter, function: (GpApiResponseData) -> Unit)
}