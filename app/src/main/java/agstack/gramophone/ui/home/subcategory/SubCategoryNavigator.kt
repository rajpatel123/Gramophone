package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.advisory.adapter.ActivityListAdapter
import agstack.gramophone.ui.advisory.adapter.CropIssueListAdapter
import agstack.gramophone.ui.advisory.adapter.RecommendedLinkedProductsListAdapter
import agstack.gramophone.ui.advisory.models.advisory.GpApiResponseData
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.Banner
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
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
        mSkuOfferList: ArrayList<PromotionListItem?>,
        productData: ProductData,
    )

    fun openProductDetailsActivity(productData: ProductData)

    fun updateOfferApplicabilityOnDialog(
        isOfferApplicable: Boolean, promotionId: String? = null, message: String,
    )

    fun updateOfferOnAddToCartDialog(
        mSkuOfferList: ArrayList<PromotionListItem?>
    )

    fun disableSortAndFilter()

    fun enableSortAndFilter()

    fun loadUrl(url: String)

    fun reload()

    fun setAdvisoryActivity(activityListAdapter: ActivityListAdapter, function: (GpApiResponseData) -> Unit,infoClicked: (GpApiResponseData) -> Unit)

    fun updateActivitiesList(it: GpApiResponseData)

    fun setAdvisoryProblemsActivity(activityListAdapter: CropIssueListAdapter, function: (agstack.gramophone.ui.advisory.models.cropproblems.GpApiResponseData) -> Unit)

    fun showInfoBottomSheet()

    fun openIssueImagesBottomSheet(it: GpApiResponseData)

    fun setProductList(recommendedLinkedProductsListAdapter: RecommendedLinkedProductsListAdapter, onAddToCartClick: ((productId: Int) -> Unit),
                       onProductDetailClick: ((productId: Int) -> Unit),)

    fun updateCartCount(cartCount: Int)
}
