package agstack.gramophone.ui.referandearn.transaction.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiTrace(

	@field:SerializedName("gp_language")
	val gpLanguage: String? = null,

	@field:SerializedName("gp_request_id")
	val gpRequestId: String? = null,

	@field:SerializedName("gp_trace_id")
	val gpTraceId: String? = null
) : Parcelable