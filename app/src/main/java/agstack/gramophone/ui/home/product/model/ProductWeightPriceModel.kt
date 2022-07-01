package agstack.gramophone.ui.home.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductWeightPriceModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("weight")
    var weight: String,

    @SerializedName("price")
    var price: String,
    @SerializedName("unit")
    var unit: String,
    @SerializedName("selected")
    var selected: Boolean = false
): Parcelable
