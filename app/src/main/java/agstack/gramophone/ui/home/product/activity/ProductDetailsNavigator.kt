package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import android.os.Bundle
import androidx.fragment.app.FragmentManager

interface ProductDetailsNavigator: BaseNavigator {
    fun getBundle(): Bundle?
    fun setToolbarTitle(title:String?)
    fun setProductSKUAdapter(productSKUAdapter: ProductSKUAdapter, onSKUItemClicked: (ProductSkuListItem) -> Unit)
    fun setProductSKUOfferAdapter(productSKUOfferAdapter: ProductSKUOfferAdapter, onOfferItemClicked: (PromotionListItem) -> Unit,onViewAllClicked:(PromotionListItem)->Unit)
    fun getFragmentManagerPager(): FragmentManager
    fun setProductImagesViewPagerAdapter(productImagesAdapter: ProductImagesAdapter)
     fun setRelatedProductsAdapter(relatedProductFragmentAdapter: RelatedProductFragmentAdapter, function: (RelatedProductItem) -> Unit)
     fun setProductDetailsAdapter(productDetailsAdapter: ProductDetailsAdapter)
     fun setRatingAndReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter)
     fun openViewAllReviewRatingsActivity(productId: Int, productReviewsData: GpApiResponseData?)
    fun openProductDetailsActivity(productData: ProductData)
    //fun openAddEditProductReviewsFragment(newInstance: GenuineCustomerRatingAlertFragment)
}