package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.adapter.SubCategoryAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.os.Bundle

interface SubCategoryNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setSubCategoryAdapter(
        subCategoryAdapter: SubCategoryAdapter
    )

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter, onAddToCartClick: ((productId: String) -> Unit))

    fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<PromotionListItem?>
    )
}
