package agstack.gramophone.ui.cart.model

data class CartDataResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_exception: GpApiException,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)

class GpApiErrorData

class GpApiException

data class GpApiResponseData(
    val cart_items: List<CartItem>,
    val cart_reference_id: String,
    val count: Int,
    val currency: String,
    val gramcash_coins: Int,
    val total: Int,
    val total_discount: Int
)

data class CartItem(
    val currency: String,
    val gramcash_available: Boolean,
    val gramcash_redeemable: Boolean,
    val language: String,
    val offer_applied: List<OfferApplied>,
    val price: String,
    val product_available: String,
    val product_id: String,
    val product_image: String,
    val product_name: String,
    val product_sku: String,
    val quantity: String,
    val total_price: String
)

data class OfferApplied(
    val discount: String,
    val offer_name: String,
    val pay_total: String,
    val redemption: Redemption,
    val tnc: Tnc,
    val valid_on_sku: String,
    val valid_till: String
)

data class Redemption(
    val `1`: String,
    val `2`: String
)

data class Tnc(
    val `1`: String,
    val `2`: String
)