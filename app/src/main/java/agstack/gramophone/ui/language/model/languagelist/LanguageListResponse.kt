package agstack.gramophone.ui.language.model.languagelist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LanguageListResponse(
    @SerializedName("gp_api_message")
    val gp_api_message: String,

    @SerializedName("gp_api_response_data")
    val gp_api_response_data: GpApiResponseData,

    @SerializedName("gp_api_status")
    val gp_api_status: String
):Parcelable