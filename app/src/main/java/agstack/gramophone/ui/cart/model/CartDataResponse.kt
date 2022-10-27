package agstack.gramophone.ui.cart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CartDataResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

class GpApiErrorData

data class GpApiResponseData(
    val cart_items: List<CartItem>,
    val cart_reference_id: String,
    val count: Int,
    val free_or_offer_product_discount_total: Float,
    val gramcash_coins: Int,
    val product_discount_total: Float,
    val promotional_discount_total: Float,
    val sub_total: String,
    val total: Float,
    val total_discount: Float,
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String,
)

data class CartItem(
    val brand_name: String,
    val promotional_discount: Float,
    val cart_product_price: String,
    val cgst_percent: String,
    val company_name: String,
    val cost_price: Float,
    val discount: String,
    val discount_percent: String,
    var discount_price: Float,
    val gramcash_redeemable: Boolean,
    val image: String,
    val item_product_discount: String,
    val price_for_cart: String,
    val product_cost_price: String,
    val product_id: String,
    val product_image: String,
    val product_name: String,
    val offer_applied: OfferApplied,
    val product_sku: String,
    var quantity: Int,
    val selling_price: String,
    val sgst_percent: String,
    val shipping_details: ShippingDetails,
    val unit_selling_price: String,
    val applicable_gramcash: Int,
)

data class OfferApplied(
    var product_name: String,
    val offer_name: String,
    val valid_on_sku: String,
    val valid_till: String,
    val t_c: String,
    val image: String,
    var promotion_id:String?=null
)

data class ShippingDetails(
    val cash_on_delivery: String,
    val free_shipping: String,
    val shipping_cost: String,
)