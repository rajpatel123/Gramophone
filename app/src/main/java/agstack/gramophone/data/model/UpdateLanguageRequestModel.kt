package agstack.gramophone.data.model

import com.google.gson.annotations.SerializedName

data class UpdateLanguageRequestModel(
    @SerializedName("language")
    val language: String
)