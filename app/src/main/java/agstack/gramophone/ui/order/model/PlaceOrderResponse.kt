package agstack.gramophone.ui.order.model

data class PlaceOrderResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: ResponseData,
    val gp_api_status: String
)

data class ResponseData(
    val order_ref_id: Int
)