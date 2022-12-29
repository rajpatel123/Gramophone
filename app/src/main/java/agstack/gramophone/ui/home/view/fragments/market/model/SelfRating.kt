package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelfRating(
	@field:SerializedName("is_certified_buyer")
	var is_certified_buyer: Boolean? = false,

	@field:SerializedName("date")
	var date: String? = null,

	@field:SerializedName("customer_address")
	var customerAddress: String? = null,

	@field:SerializedName("product_id")
	var productId: Int? = null,

	@field:SerializedName("rating")
	var rating: Double? = null,

	@field:SerializedName("comments")
	var comment: String? = null,

	@field:SerializedName("customer_name")
	var customerName: String? = null,

	@field:SerializedName("customer_id")
	var customerId: Int? = null
) : Parcelable