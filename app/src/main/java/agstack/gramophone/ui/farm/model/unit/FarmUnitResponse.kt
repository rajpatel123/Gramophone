package agstack.gramophone.ui.farm.model.unit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class FarmUnitResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String?,
    val gp_api_response_data: List<GpApiResponseData>,
    val gp_api_status: String?,
    val gp_api_trace: GpApiTrace?
)

data class GpApiErrorData(
    val error_code: String?
)


data class GpApiResponseData(
    val unit: String?,
    val unit_id: String?
){
    @Override
    override fun toString(): String {
        return unit!!
    }
}

data class GpApiTrace(
    val gp_language: String?,
    val gp_request_id: String?,
    val gp_trace_id: String?
)