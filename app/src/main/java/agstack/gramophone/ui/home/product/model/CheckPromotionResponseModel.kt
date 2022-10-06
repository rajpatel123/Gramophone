package agstack.gramophone.ui.home.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckPromotionResponseModel(

	@field:SerializedName("gp_api_response_data")
	val gpApiResponseData: GpApiResponseData? = null,

	@field:SerializedName("gp_api_status")
	val gpApiStatus: String? = null,

	@field:SerializedName("gp_api_message")
	val gpApiMessage: String? = null
) : Parcelable