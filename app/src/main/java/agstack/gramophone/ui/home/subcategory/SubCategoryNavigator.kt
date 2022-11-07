package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import android.os.Bundle

interface SubCategoryNavigator : BaseNavigator {
    fun getBundle(): Bundle?

    fun setViewPagerAdapter(bannerList: List<Banner>?)

    fun setSubCategoryAdapter(
        subCategoryAdapter: ShopByCategoryAdapter,
    )

    fun getSubCategoryDetail(categoryId: String, subCategoryId: String, subCategoryName: String, subCategoryImage: String)

    fun setProductListAdapter(
        productListAdapter: ProductListAdapter,
        onAddToCartClick: ((productId: Int) -> Unit),
        onProductDetailClick: ((productId: Int) -> Unit),
    )

    fun openAddToCartDialog(
        mSKUList: ArrayList<ProductSkuListItem?>,
        mSkuOfferList: ArrayList<Offer>,
        productData: ProductData,
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean, promotionId: String? = null, message: String,
    )

    fun disableSortAndFilter()

    fun enableSortAndFilter()

    fun loadUrl(url: String)

    fun reload()
}
