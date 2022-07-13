package agstack.gramophone.ui.verifyotp.model

data class ValidateOtpResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)