package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShippingDetails(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("free_shipping")
	val freeShipping: Boolean? = null,

	@field:SerializedName("shipping_cost")
	val shippingCost: String? = null,

	@field:SerializedName("estimated_delivery")
	val estimatedDelivery: String? = null
) : Parcelable