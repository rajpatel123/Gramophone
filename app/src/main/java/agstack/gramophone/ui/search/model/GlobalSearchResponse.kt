package agstack.gramophone.ui.search.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GlobalSearchResponse(
    var gp_api_error_data: GpApiErrorData?,
    var gp_api_message: String?,
    var gp_api_response_data: GpApiResponseData?,
    var gp_api_status: String?,
    var gp_api_trace: GpApiTrace?
) : Parcelable

@Parcelize
data class GpApiResponseData(
    var data: List<Data>,
    var meta: Meta?
) : Parcelable

@Parcelize
data class Data(
    var items: List<Item>,
    var type: String?,
    val afterKey : AfterKey
) : Parcelable

@Parcelize
data class Meta(
    var pages: Int?
) : Parcelable


@Parcelize
data class AfterKey(
    @field:SerializedName("product_base_name.keyword")
    var product_base_name: String?
) : Parcelable

@Parcelize
data class Item(
    @SerializedName(value = "@timestamp") var timestamp: String?,
    @SerializedName(value = "@version") var version: String?,
    var handle: String?,
    var highest_group_score: String?,
    var id: String?,
    var category_parent_id: String?,
    var image: String?,
    var postImage : String?,
    var name: String?,
    var product_base_name: String?,
    var product_sku_list: List<ProductSku>?,
    var related_tags: List<String>?,
    var status: String?,
    var likesCount: Int?,
    var isLiked : Boolean = false,  // local
    var commentsCount: Int?,
    var description: String?,
    var actionTimeStamp: Long?,
    var createdDate : String?,
    var language: String?,
    var index_id: String?,
    var category_type: String?
) : Parcelable

@Parcelize
data class ProductSku(
    var brand_name: String?,
    var brand_name_id: String?,
    var cgst_percent: String?,
    var company_id: String?,
    var company_name: String?,
    var cost_price: Float?,
    var currency: String?,
    var discount_price: Float?,
    var gramcash_redeemable: Boolean?,
    var product_app_name: String?,
    var product_base_name: String?,
    var product_brand_name: String?,
    var product_cost_price: Float?,
    var product_currency: String?,
    //var product_details: ProductDetails,
    var product_featured_flag: String?,
    var product_id: Int?,
    var product_image: String?,
    var product_images: List<String>?,
    var product_name: String?,
    var product_score: String?,
    var product_sku: String?,
    var product_type_id: String?,
    var product_type_name: String?,
    var product_unit: String?,
    var product_video_url: String?,
    var retailer_trade_licence: String?,
    var sales_focus_for_customer: String?,
    var sales_focus_for_retailer: String?,
    var selling_price: Float?,
    var sgst_percent: String?,
    var shipping_details: ShippingDetails?
) : Parcelable

@Parcelize
data class ProductDetails(
    @SerializedName(value = "Key Points") var KeyPoints: List<KeyPoint>?
) : Parcelable

@Parcelize
data class ShippingDetails(
    var cash_on_delivery: String?,
    var free_shipping: String?,
    var shipping_cost: String?
) : Parcelable

@Parcelize
data class KeyPoint(
    var product_detail_key: String?,
    var product_detail_type: String?,
    var product_detail_value: String?
) : Parcelable