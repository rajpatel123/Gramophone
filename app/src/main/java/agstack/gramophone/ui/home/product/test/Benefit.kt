package agstack.gramophone.ui.home.product.test

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Benefit(

    @field:SerializedName("promotion_type")
    val promotionType: String? = null,

    @field:SerializedName("amount")
    val amount_saved: Double? = 0.0,

    @field:SerializedName("freebie_text")
    val freebieText: String? = null
) : Parcelable