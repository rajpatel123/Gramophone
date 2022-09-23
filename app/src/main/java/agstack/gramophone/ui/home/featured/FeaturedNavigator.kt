package agstack.gramophone.ui.home.featured

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ProductListAdapter

interface FeaturedNavigator : BaseNavigator {

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter
    )
}
