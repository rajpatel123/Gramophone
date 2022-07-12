package agstack.gramophone.ui.verifyotp.model

data class GpApiResponseData(
    val is_address: Boolean,
    val token: String,
    val uuid: String
)