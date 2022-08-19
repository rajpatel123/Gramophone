package agstack.gramophone.ui.verifyotp.model

data class ValidateOtpRequestModel(
    val mobile_no: String,
    val otp: String,
    val otp_reference_id:Int
)