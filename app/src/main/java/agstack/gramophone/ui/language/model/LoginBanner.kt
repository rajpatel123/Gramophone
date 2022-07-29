package agstack.gramophone.ui.language.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginBanner(
    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("title")
    val title: String
):Parcelable