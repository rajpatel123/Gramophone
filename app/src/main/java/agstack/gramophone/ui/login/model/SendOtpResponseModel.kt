package agstack.gramophone.ui.login.model

data class SendOtpResponseModel(
    val gp_api_error_data: List<Any>,
    val gp_api_exception: List<Any>,
    val gp_api_message: String,
    val gp_api_response_data: List<Any>,
    val gp_api_status: String
)