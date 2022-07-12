package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("gramcash_redeemable")
	val gramcashRedeemable: Boolean? = null,

	@field:SerializedName("cgst_percent")
	val cgstPercent: String? = null,

	@field:SerializedName("discount_price")
	val discountPrice: String? = null,

	@field:SerializedName("product_featured_flag")
	val productFeaturedFlag: Int? = null,

	@field:SerializedName("shipping_details")
	val shippingDetails: ShippingDetails? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("how_to_use")
	val howToUse: String? = null,

	@field:SerializedName("retailer_trade_licence")
	val retailerTradeLicence: String? = null,

	@field:SerializedName("technical_details")
	val technicalDetails: List<String?>? = null,

	@field:SerializedName("product_images")
	val productImages: List<String?>? = null,

	@field:SerializedName("product_sku")
	val productSku: String? = null,

	@field:SerializedName("product_base_name")
	val productBaseName: String? = null,

	@field:SerializedName("sgst_percent")
	val sgstPercent: String? = null,

	@field:SerializedName("product_type_name")
	val productTypeName: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("product_currency")
	val productCurrency: String? = null,

	@field:SerializedName("user_favourite")
	val userFavourite: Boolean? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("cost_price")
	val costPrice: Double? = null,


	@field:SerializedName("self_rating")
	val selfRating: SelfRating? = null,

	@field:SerializedName("company_id")
	val companyId: String? = null,

	@field:SerializedName("product_brand_name")
	val productBrandName: String? = null,

	@field:SerializedName("brand_name")
	val brandName: String? = null,

	@field:SerializedName("product_type_id")
	val productTypeId: String? = null,

	@field:SerializedName("product_unit")
	val productUnit: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("review_list")
	val reviewList: List<ReviewListItem?>? = null,

	@field:SerializedName("product_sku_list")
	val productSkuList: List<ProductSkuListItem?>? = null,

	@field:SerializedName("offers")
	val offers: List<ProductSkuOfferItem?>? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("product_details")
	val productDetails: List<String?>? = null,

	@field:SerializedName("product_cost_price")
	val productCostPrice: Double? = null,

	@field:SerializedName("product_available")
	val productAvailable: Int? = null,

	@field:SerializedName("brand_name_id")
	val brandNameId: String? = null,

	@field:SerializedName("product_app_name")
	val productAppName: String? = null,

	@field:SerializedName("gramcash_available")
	val gramcashAvailable: Boolean? = null
) : Parcelable