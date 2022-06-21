package agstack.gramophone.ui.language.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("android_id_hashed") val android_id_hashed: String,
    @SerializedName("session_token") val session_token: String
)