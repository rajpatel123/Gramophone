package agstack.gramophone.ui.language.model.languagelist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    @SerializedName("language")
    val language: String,

    @SerializedName("language_code")
    val language_code: String,

    @SerializedName("language_display")
    val language_display: String,

    @SerializedName("selected")
    var selected:Boolean
):Parcelable