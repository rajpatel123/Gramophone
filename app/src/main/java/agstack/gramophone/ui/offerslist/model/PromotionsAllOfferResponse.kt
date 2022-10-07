package agstack.gramophone.ui.offerslist.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PromotionsAllOfferResponse(

	/*@field:SerializedName("gp_api_error_data")
	val gpApiErrorData: List<Objects?>? = null,*/

	@field:SerializedName("gp_api_response_data")
	val gpApiResponseData: GpApiResponseData? = null,

	@field:SerializedName("gp_api_status")
	val gpApiStatus: String? = null,

	@field:SerializedName("gp_api_message")
	val gpApiMessage: String? = null
/*
	@field:SerializedName("gp_api_exception")
	val gpApiException: List<Objects?>? = null*/
) : Parcelable