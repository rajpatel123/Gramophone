package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData

interface MarketFragmentNavigator :BaseNavigator {


    fun setFeaturedProductsAdapter(adapter: ProductListAdapter, onProductListItemClick: (ProductData) -> Unit)
     fun startProductDetailsActivity(it: ProductData)
}