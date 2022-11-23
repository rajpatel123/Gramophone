package agstack.gramophone.ui.articles

data class SuggestedCropResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseData(
    val crops_suggested: List<CropsSuggested>
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)

data class CropsSuggested(
    val crop_id: Int,
    val crop_image: String,
    val crop_name: String
)