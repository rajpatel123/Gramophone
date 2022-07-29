package agstack.gramophone.ui.language.model.languagelist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(
    @SerializedName("language_list")
    val language_list: List<Language>
):Parcelable