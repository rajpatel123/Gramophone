package agstack.gramophone.ui.address.model

data class UpdateAddressRequestModel(
    val address: String?,
    val district: String?,
    val pin_code: String?,
    val state: String?,
    val tehsil: String?,
    val village: String?
)