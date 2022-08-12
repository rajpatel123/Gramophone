package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.adapter.SubCategoryAdapter

interface SubCategoryNavigator : BaseNavigator {
    fun setSubCategoryAdapter(
        subCategoryAdapter: SubCategoryAdapter
    )

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter
    )
}
