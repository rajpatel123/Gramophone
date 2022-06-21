package agstack.gramophone.ui.language.model

import com.google.gson.annotations.SerializedName

data class RegisterDeviceModel(
    @SerializedName("data") val `data`: Data
)