package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDataResponse(
/*
	@field:SerializedName("gp_api_error_data")
	val gpApiErrorData: GpApiErrorData? = null,*/

	@field:SerializedName("gp_api_response_data")
	val gpApiResponseData: GpApiResponseData? = null,

	@field:SerializedName("gp_api_status")
	val gpApiStatus: String? = null,

	@field:SerializedName("gp_api_message")
	val gpApiMessage: String? = null,

	@field:SerializedName("gp_api_exception")
	val gpApiException: GpApiException? = null
) : Parcelable