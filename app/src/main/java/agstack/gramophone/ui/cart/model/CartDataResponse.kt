package agstack.gramophone.ui.cart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CartDataResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseData(
    val cart_items: List<CartItem>,
    val cart_reference_id: String,
    val count: Int,
    val free_or_offer_product_discount_total: Int,
    val gramcash_coins: Int,
    val product_discount_total: Int,
    val promotional_discount_total: Int,
    val sub_total: Int,
    val total: Int,
    val total_discount: Int
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)

data class CartItem(
    val brand_name: String,
    val cart_product_price: Int,
    val cgst_percent: String,
    val company_name: String,
    val cost_price: String,
    val discount: String,
    val discount_percent: Int,
    val discount_price: String,
    val gramcash_redeemable: Boolean,
    val image: String,
    val item_product_discount: Int,
    val price_for_cart: String,
    val product_cost_price: String,
    val product_id: String,
    val product_image: String,
    val product_name: String,
    val offer_applied: List<OfferApplied>,
    val product_sku: String,
    var quantity: Int,
    val selling_price: String,
    val sgst_percent: String,
    val shipping_details: ShippingDetails,
    val unit_selling_price: String
)

data class OfferApplied(
    var product_name: String,
    val discount: String,
    val offer_name: String,
    val pay_total: String,
    val valid_on_sku: String,
    val valid_till: String
)

data class ShippingDetails(
    val cash_on_delivery: String,
    val free_shipping: String,
    val shipping_cost: String
)