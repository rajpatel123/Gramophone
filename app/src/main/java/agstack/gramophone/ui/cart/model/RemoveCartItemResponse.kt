package agstack.gramophone.ui.cart.model

data class RemoveCartItemResponse(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)