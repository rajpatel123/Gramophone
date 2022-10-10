package agstack.gramophone.ui.farm.model

data class FarmUnitResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<String>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)
