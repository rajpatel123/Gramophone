package agstack.gramophone.ui.dialog.filter

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainFilterData(
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("name")
    var name: String,
    @SerializedName("count")
    var count: Int,
) : Parcelable
