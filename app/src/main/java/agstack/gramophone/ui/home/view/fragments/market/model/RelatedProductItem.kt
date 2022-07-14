package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelatedProductItem(

	@field:SerializedName("product_base_name")
	val productBaseName: String? = null,

	@field:SerializedName("product_image")
	val productImage: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("sales_price")
	val salesPrice: String? = null,

	@field:SerializedName("total_rating")
	val totalRating: String? = null,

	@field:SerializedName("mrp_price")
	val mrpPrice: String? = null
) : Parcelable