package agstack.gramophone.ui.referandearn.transaction.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GramCashTxnResponseModel(

	@field:SerializedName("gp_api_error_data")
	val gpApiErrorData: GpApiErrorData? = null,

	@field:SerializedName("gp_api_response_data")
	val gpApiResponseData: List<GpApiResponseDataItem?>? = null,

	@field:SerializedName("gp_api_status")
	val gpApiStatus: String? = null,

	@field:SerializedName("gp_api_trace")
	val gpApiTrace: GpApiTrace? = null,

	@field:SerializedName("gp_api_message")
	val gpApiMessage: String? = null
) : Parcelable