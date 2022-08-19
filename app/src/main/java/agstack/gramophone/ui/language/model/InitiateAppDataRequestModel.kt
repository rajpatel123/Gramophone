package agstack.gramophone.ui.language.model

data class InitiateAppDataRequestModel(
    val device_details: DeviceDetails,
    val language: String
)