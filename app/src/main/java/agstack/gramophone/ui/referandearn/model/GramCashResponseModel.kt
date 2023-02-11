package agstack.gramophone.ui.referandearn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GramCashResponseModel(

	@field:SerializedName("gp_api_error_data")
	val gpApiErrorData: GpApiErrorData? = null,

	@field:SerializedName("gp_api_response_data")
	val gpApiResponseData: GpApiResponseData? = null,

	@field:SerializedName("gp_api_status")
	val gpApiStatus: String? = null,

	@field:SerializedName("gp_api_trace")
	val gpApiTrace: GpApiTrace? = null,

	@field:SerializedName("gp_api_message")
	val gpApiMessage: String? = null,

	@field:SerializedName("referral_and_share_image")
	val referralAndShareImage: String? = null
) : Parcelable