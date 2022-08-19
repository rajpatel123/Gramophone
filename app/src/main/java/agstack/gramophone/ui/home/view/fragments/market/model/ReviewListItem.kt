package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewListItem(

	@field:SerializedName("date")
	var date: String? = null,

	@field:SerializedName("customer_address")
	val customerAddress: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("customer_name")
	val customerName: String? = null,

	@field:SerializedName("customer_id")
	val customerId: Int? = null


) : Parcelable