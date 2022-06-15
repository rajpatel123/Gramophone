package agstack.gramophone.ui.language.model

data class RegistrerDeviceRquestModel(
    val app_version_code: Int,
    val app_version_name: String,
    val device_id: String,
    val device_model: String,
    val one_signal_id: String,
    val os_version: Int,
    val token: String
)