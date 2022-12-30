package agstack.gramophone.ui.home.featured.ratingeligibility

data class RatingEligibilityResponseModel(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)