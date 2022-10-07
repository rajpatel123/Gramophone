package agstack.gramophone.ui.dialog.sortby

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SortByData(
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("name")
    var name: String,
    @SerializedName("sortByCode")
    var sortByCode: String,
) : Parcelable