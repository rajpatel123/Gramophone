package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.product.ProductDetailsAdapter
import agstack.gramophone.ui.home.product.fragment.ContactForPriceBottomSheetDialog
import agstack.gramophone.ui.home.product.fragment.ExpertAdviceBottomSheetFragment
import agstack.gramophone.ui.home.product.fragment.GenuineCustomerRatingAlertFragment
import agstack.gramophone.ui.home.product.fragment.RelatedProductFragmentAdapter
import agstack.gramophone.ui.home.subcategory.AvailableProductOffersAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.home.view.fragments.market.model.sku.ProductCategory
import android.os.Bundle
import androidx.fragment.app.FragmentManager

interface ProductDetailsNavigator : BaseNavigator {
    fun getBundle(): Bundle?
    fun setToolbarTitle(title: String?)
    fun initializeYoutube(videoId: String?)
    fun setProductSKUAdapter(
        productSKUAdapter: ProductSKUAdapter,
        onSKUItemClicked: (ProductSkuListItem) -> Unit,
    )

    fun setProductSKUOfferAdapter(
        productSKUOfferAdapter: AvailableProductOffersAdapter,
        offerListSize: Int,
    )

    fun getFragmentManagerPager(): FragmentManager
    fun setProductImagesViewPagerAdapter(productImagesAdapter: ProductImagesAdapter)
    fun setRelatedProductsAdapter(
        relatedProductFragmentAdapter: RelatedProductFragmentAdapter,
        relatedProductItemClicked: (RelatedProductItem) -> Unit,
    )

    fun setProductDetailsAdapter(productDetailsAdapter: ProductDetailsAdapter)
    fun setRatingAndReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter)
    fun openViewAllReviewRatingsActivity(productId: Int, productReviewsData: GpApiResponseData?)
    fun openProductDetailsActivity(productData: ProductData)
    fun showGenuineCustomerRatingDialog(
        genuineCustomerRatingAlertFragment: GenuineCustomerRatingAlertFragment,
        addToCartEnabled: Boolean,
        onAddToCartClick: () -> Unit,
    )

    fun showExpertAdviceDialog(
        expertAdviceBottomSheetFragment: ExpertAdviceBottomSheetFragment,
        onOkayClick: () -> Unit,
        onCancelClick: () -> Unit,
    )

    fun dismissExpertBottomSheet()
    fun refreshSKUAdapter()
    fun updateUIAfterCalculation(
        productName: String,
        salesPrice: Float,
        mrpPrice: Float,
        discount: String,
        isDiscountPercentVisible: Boolean,
        isMRPVisible: Boolean,
        isOffersLayoutVisible: Boolean,
        isContactForPriceVisible: Boolean,
    )

    fun setRatingBarChangeListener()
    fun refreshOfferAdapter()
    fun showContactForPriceBottomSheetDialog(contactForPriceBottomSheetDialog: ContactForPriceBottomSheetDialog)
    fun updateAddToCartButtonText(text: String? = null): String
    fun updateCartCount(cartCount: Int)
    fun showRating()
    fun sendProductViewMoEngageEvent(
        productId: Int,
        productBaseName: String,
        avgRating: Double,
        redirectionSource: String,
        customerId: String,
        productCategory: List<ProductCategory>
    )
    fun sendFavouriteMoEngageEvent(productId: Int, productBaseName: String, customerId: String, isFavourite: Boolean)
}