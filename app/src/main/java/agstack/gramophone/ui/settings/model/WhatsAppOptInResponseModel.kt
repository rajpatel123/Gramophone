package agstack.gramophone.ui.settings.model

data class WhatsAppOptInResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)