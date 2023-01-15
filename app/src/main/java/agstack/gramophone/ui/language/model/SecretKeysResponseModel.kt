package agstack.gramophone.ui.language.model

data class SecretKeysResponseModel(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseDataX,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)