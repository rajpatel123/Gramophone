package agstack.gramophone.ui.tv.model

data class BookmarkedListResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseData(
    val bookmarks: List<Bookmark>
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)

data class Bookmark(
    val _id: String,
    val activity_type: String,
    val created_at: String,
    val customer_id: String,
    val status: Int,
    val updated_at: String,
    val youtube_video_id: String
)