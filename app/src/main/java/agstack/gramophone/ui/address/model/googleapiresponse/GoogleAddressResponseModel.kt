package agstack.gramophone.ui.address.model.googleapiresponse

data class GoogleAddressResponseModel(
    val plus_code: PlusCode,
    val results: List<Result>,
    val status: String
)