package agstack.gramophone.ui.createnewpost.view.model.hashtags

data class HasgTagResponseModel(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)