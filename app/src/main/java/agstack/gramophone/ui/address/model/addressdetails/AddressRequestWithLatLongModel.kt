package agstack.gramophone.ui.address.model.addressdetails

data class AddressRequestWithLatLongModel(
    val latitude: String,
    val longitude: String,
    val state: String,
    val district: String,
    val tehsil: String?,
    val village: String?,
    val pincode: String
)