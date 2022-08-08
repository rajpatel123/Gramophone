package agstack.gramophone.ui.settings.model.blockedusers

data class BlockedUsersListResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)