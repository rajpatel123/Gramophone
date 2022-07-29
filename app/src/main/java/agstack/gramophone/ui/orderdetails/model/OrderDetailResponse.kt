package agstack.gramophone.ui.orderdetails.model

data class OrderDetailResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_exception: GpApiException,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData = GpApiResponseData("", "", "", "","", emptyList(), ""),
    val gp_api_status: String
)

class GpApiErrorData

class GpApiException

data class GpApiResponseData(
    val order_date: String = "",
    val order_id: String = "",
    val order_status: String = "",
    val payment_method: String = "",
    val price: String = "",
    val products: List<Product> = emptyList(),
    val quantity: String = ""
)

data class Product(
    val delivery_address: DeliveryAddress,
    val offer_applied: List<OfferApplied>,
    val price: String,
    val product_id: Int,
    val product_image: String,
    val product_name: String,
    val product_sku: String,
    val product_unit: String,
    val quantity: Int
)

data class DeliveryAddress(
    val address: String,
    val mobile: String,
    val name: String
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