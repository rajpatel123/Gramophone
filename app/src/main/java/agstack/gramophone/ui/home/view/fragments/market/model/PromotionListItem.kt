package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromotionListItem(


    @field:SerializedName("promotion_id")
    val promotion_id: Int? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("applicable_on_sku")
    val applicable_on_sku: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("t_c")
    val t_c: String? = null,

    @field:SerializedName("amount_saved")
    val amount_saved: Double? = null,
    @field:SerializedName("selected")
    var selected :Boolean?=false
) : Parcelable