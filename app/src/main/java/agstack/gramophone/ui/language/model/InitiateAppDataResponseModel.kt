package agstack.gramophone.ui.language.model

data class InitiateAppDataResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)