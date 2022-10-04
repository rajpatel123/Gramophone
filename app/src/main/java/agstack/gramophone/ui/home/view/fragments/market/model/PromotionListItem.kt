package agstack.gramophone.ui.home.view.fragments.market.model

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

    ) : Parcelable


/* val applicable_on_sku: String? = null,
    val benefitinrupes: Float,
    val end_date: String,
    val id: Int,
    val image: String,
    val product: Product,
    val product_base_name: String,
    val product_id: Int,
    val promotion_id: Int,
    val promotion_sub_type_id: Int,
    val promotion_type_id: Int,
    val t_c: String,
    val title: String,
    var selected: Boolean? = false,*/