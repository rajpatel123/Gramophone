package agstack.gramophone.ui.language.model.languagelist

import com.google.gson.annotations.SerializedName

data class Language(
    val language: String,
    val language_code: String,
    val language_display: String,

    @SerializedName("selected")
    var selected:Boolean
)