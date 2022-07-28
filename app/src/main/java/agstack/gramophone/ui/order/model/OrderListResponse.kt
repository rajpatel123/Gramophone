package agstack.gramophone.ui.order.model

data class OrderListResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_exception: GpApiException,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)

class GpApiErrorData

class GpApiException

data class GpApiResponseData(
    val order_list: OrderList
)

data class OrderList(
    val `data`: List<Data>,
    val links: Links,
    val meta: Meta
)

data class Data(
    val order_date: String,
    val order_id: String,
    val order_status: String,
    val payment_method: String,
    val price: String,
    val quantity: String
)

data class Links(
    val first: String,
    val last: String,
    val next: Any,
    val prev: Any
)

data class Meta(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)