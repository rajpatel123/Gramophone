package agstack.gramophone.ui.address.model.googleapiresponse

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)