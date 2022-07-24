package agstack.gramophone.data.model

data class UpdateLanguageResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)