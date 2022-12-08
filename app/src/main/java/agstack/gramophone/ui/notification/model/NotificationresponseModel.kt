package agstack.gramophone.ui.notification.model

data class NotificationresponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)