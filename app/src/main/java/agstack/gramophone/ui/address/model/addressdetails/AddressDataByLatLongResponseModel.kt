package agstack.gramophone.ui.address.model.addressdetails

data class AddressDataByLatLongResponseModel(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String
)