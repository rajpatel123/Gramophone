package agstack.gramophone.ui.advisory.models.recomondedproducts

data class RecommendedProductResponseModel(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)