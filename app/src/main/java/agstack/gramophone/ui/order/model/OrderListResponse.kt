package agstack.gramophone.ui.order.model

data class OrderListResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseData(
    val current_page: Int,
    val data: List<Data>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)

data class Data(
    val order_date: String,
    val order_id: Int,
    val order_status_name: String,
    val payment_method: String,
    val price: Int,
    val product_image: String,
    val quantity: Int,
    val vr_info: List<Any>
)

data class Link(
    val active: Boolean,
    val label: String,
    val url: String
)