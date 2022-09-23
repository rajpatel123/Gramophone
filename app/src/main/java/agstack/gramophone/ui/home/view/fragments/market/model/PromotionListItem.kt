package agstack.gramophone.ui.home.view.fragments.market.model

import agstack.gramophone.ui.cart.model.Redemption
import agstack.gramophone.ui.cart.model.Tnc
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromotionListItem(
    /*WARNING :: DO NOT REMOVE ANY FIELD FROM HERE.
    * ANY PERSON CAN ADD NEW FIELD BUT
    * DON'T REMOVE THE EXISTING FIELD
    */

    @field:SerializedName("promotion_id")
    val promotion_id: Int? = 0,

    @field:SerializedName("product_id")
    val productId: Int? = 0,

    @field:SerializedName("title")
    var title: String? = "",

    @field:SerializedName("image")
    val image: String? = "",

    @field:SerializedName("t_c")
    val t_c: String? = "",

    @field:SerializedName("amount_saved")
    val amount_saved: Double? = 0.0,

    @field:SerializedName("selected")
    var selected: Boolean? = false,

    @field:SerializedName("product_name")
    var product_name: String? = "",

    @field:SerializedName("applicable_on_sku")
    var applicable_on_sku: String? = "",

    @field:SerializedName("valid_till")
    var valid_till: String? = "",

    @field:SerializedName("tnc")
    var tnc: Tnc? = null,

    @field:SerializedName("redemption")
    var redemption: Redemption? = null,

    ) : Parcelable