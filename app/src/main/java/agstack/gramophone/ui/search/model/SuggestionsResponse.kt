package agstack.gramophone.ui.search.model

data class SuggestionsResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<String>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

data class GpApiErrorData(
    val error_code: String
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)