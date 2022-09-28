package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailsItem(

	@field:SerializedName("product_detail_type")
	val productDetailType: String? = null,

	@field:SerializedName("product_detail_key")
	val productDetailKey: String? = null,

	@field:SerializedName("product_detail_value")
	val productDetailValue: String? = null
) : Parcelable