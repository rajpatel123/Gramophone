package agstack.gramophone.ui.language.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HelpDataList(
    @SerializedName("customer_support_no")
    val customer_support_no: String
):Parcelable