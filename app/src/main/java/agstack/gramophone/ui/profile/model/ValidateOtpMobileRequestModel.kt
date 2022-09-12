package agstack.gramophone.ui.profile.model


data class ValidateOtpMobileRequestModel(
    val otp: String,
    val otp_reference_id:String
)