package agstack.gramophone.ui.home.featured

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData

interface FeaturedNavigator : BaseNavigator {

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter, onAddToCartClick: ((productId: Int) -> Unit), onProductDetailClick: ((productId: Int) -> Unit),
    )

    fun openProductDetailsActivity(productData: ProductData)
}
