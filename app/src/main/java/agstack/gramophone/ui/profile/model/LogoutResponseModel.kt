package agstack.gramophone.ui.profile.model

data class LogoutResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)