package agstack.gramophone.ui.orderdetails.model


data class OrderDetailResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

class GpApiErrorData

data class GpApiResponseData(
    val delivery_address: DeliveryAddress,
    val items: Int,
    val message: String,
    val order_date: String,
    val order_id: Int,
    val order_status: String,
    val order_type: String,
    val pricing_details: PricingDetails,
    val product_image: String,
    val products: List<Product>,
    val vr_info: VRInfo,
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String,
)

data class DeliveryAddress(
    val address: Address,
    val mobile: String,
    val name: String,
)

data class Address(
    val state: String,
    val tehsil: String,
    val district: String,
    val village: String,
    val pincode: String,
    val address_line_1: String,
)

data class PricingDetails(
    val additional_discount: Int,
    val coupon_discount: Int,
    val delivery_charge: Int,
    val gram_cash: Int,
    val product_discount: Int,
    val promotional_discount: Int,
    val sub_total_price: Int,
    val total_price: Int,
)

data class Product(
    val discounted_price: Int,
    val free_products: List<FreeProduct>,
    val is_offer_applied: String,
    val price: Int,
    val product_id: Int,
    val product_image: String,
    val product_name: String,
    val product_sku: String,
    val quantity: Int,
)

data class VRInfo(
    val name: String,
    val mobile_no: String,
)

data class FreeProduct(
    val mrp_price: Int,
    val price: Int,
    val product_image: String,
    val product_name: String,
)