package agstack.gramophone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubCatEvent(
    var category_event: String,
    var sub_category: String?="",
    val product_name: String?="",
    val product_id: String?="",
    val quantity: String?=""
):Parcelable
