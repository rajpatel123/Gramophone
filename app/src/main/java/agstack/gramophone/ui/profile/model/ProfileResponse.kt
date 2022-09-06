package agstack.gramophone.ui.profile.model

data class ProfileResponse(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseProfileData,
    val gp_api_status: String,
)

data class GpApiResponseProfileData(
    val address: Any,
    val customer_name: String,
    val mobile_no: String,
    val firm_name: Any,
    val is_farmer: Boolean,
    val is_trader: Boolean,
    val profile_image: String,
    val username: String
)
