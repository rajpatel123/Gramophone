package agstack.gramophone.ui.home.view.fragments.market.model

data class HomeDataResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseHomeData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseHomeData(
    val home_screen_sequence: List<String> = ArrayList()
)

data class GpApiTrace(
    val gp_request_id: String,
    val gp_trace_id: String
)