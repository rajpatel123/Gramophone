package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class ProductSkuListItem(

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("show_price_for_retailer")
	val showPriceForRetailer: Boolean? = null,

	@field:SerializedName("retailer_order_quantity_max")
	val retailerOrderQuantityMax: Int? = null,

	@field:SerializedName("retailer_order_quantity_min")
	val retailerOrderQuantityMin: Int? = null,

	@field:SerializedName("retailer_orderability")
	val retailerOrderability: Boolean? = null,

	@field:SerializedName("sku")
	val sku: String? = null,

	@field:SerializedName("retailer_visibility")
	val retailerVisibility: Boolean? = null,
	@SerializedName("selected")
	var selected: Boolean = false,

	@SerializedName("sku_price")
	var sku_price: Double? = null
) : Parcelable