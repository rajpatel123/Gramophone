package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("offers")
	val offers: List<OffersItem?>? = null,

	@field:SerializedName("self_rating")
	val selfRating: SelfRating? = null,

	@field:SerializedName("related_product")
	val relatedProduct: List<RelatedProductItem?>? = null,

	@field:SerializedName("is_user_favourite")
	val isUserFavourite: Boolean? = null,

	@field:SerializedName("product_featured_flag")
	val productFeaturedFlag: Boolean? = null,

	@field:SerializedName("shipping_details")
	val shippingDetails: ShippingDetails? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("how_to_use")
	val howToUse: HowToUse? = null,

	@field:SerializedName("review_list")
	val reviewList: List<ReviewListItem?>? = null,

	@field:SerializedName("product_images")
	val productImages: List<String?>? = null,

	@field:SerializedName("product_base_name")
	val productBaseName: String? = null,

	@field:SerializedName("product_sku_list")
	val productSkuList: List<ProductSkuListItem?>? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("product_details")
	val productDetails: ProductDetails? = null,

	@field:SerializedName("product_id_default")
	val productIdDefault: String? = null,

	@field:SerializedName("product_available")
	val productAvailable: Int? = null
) : Parcelable