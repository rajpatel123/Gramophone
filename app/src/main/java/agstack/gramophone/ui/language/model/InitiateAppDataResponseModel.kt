package agstack.gramophone.ui.language.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class InitiateAppDataResponseModel(
    @SerializedName("gp_api_message")
    val gp_api_message: String,
    @SerializedName("gp_api_response_data")
    val gp_api_response_data: GpApiResponseData,

    @SerializedName("gp_api_status")
    val gp_api_status: String
):Parcelable