package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OffersItem(

	@field:SerializedName("applicable_on_sku")
	val applicableOnSku: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount_saved")
	val amountSaved: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("t_c")
	val tC: String? = null,

	@field:SerializedName("promotion_id")
	val promotionId: String? = null,

	@field:SerializedName("title")
	val title: String? = null,
	@field:SerializedName("selected")
	var selected :Boolean?=false
) : Parcelable