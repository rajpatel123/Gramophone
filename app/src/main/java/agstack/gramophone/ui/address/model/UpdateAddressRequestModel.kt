package agstack.gramophone.ui.address.model

data class UpdateAddressRequestModel(
    val address: String?,
    val district: String?,
    val pincode : String?,
    val state: String?,
    val tehsil: String?,
    val village: String?
)