package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductSkuListItem(

	@field:SerializedName("product_sku")
	val productSku: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("sales_price")
	val salesPrice: String? = null,

	@field:SerializedName("mrp_price")
	val mrpPrice: String? = null,
	@field:SerializedName("selected")
	var selected: Boolean? = false,
	@field:SerializedName("product_app_name")
	var product_app_name: String?=null
) : Parcelable