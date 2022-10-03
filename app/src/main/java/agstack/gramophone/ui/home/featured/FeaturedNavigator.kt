package agstack.gramophone.ui.home.featured

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.model.GpApiOfferResponse
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem

interface FeaturedNavigator : BaseNavigator {

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter, onAddToCartClick: ((productId: Int) -> Unit), onProductDetailClick: ((productId: Int) -> Unit),
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<Offer>,
        productData: ProductData,
    )

    fun updateAddToCartDialog(
        isShowError: Boolean,
        errorMsg: String,
        appliedOfferResponse: GpApiOfferResponse,
    )
}
