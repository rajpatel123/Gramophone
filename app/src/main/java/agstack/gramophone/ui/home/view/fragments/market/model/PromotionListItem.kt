package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromotionListItem(

    @field:SerializedName("promotion_id")
    val promotion_id: Int? = 0,

    @field:SerializedName("title")
    var title: String? = "",

    @field:SerializedName("applicable_on_sku")
    var applicable_on_sku: String? = "",


    @field:SerializedName("image")
    val image: String? = "",

    @field:SerializedName("t_c")
    val t_c: String? = "",

    @field:SerializedName("end_date")
    var valid_till: String? = "",

    @field:SerializedName("selected")
    var selected: Boolean? = false,

    @field:SerializedName("amount_saved")
    val amount_saved: Double? = 0.0,

    ) : Parcelable


