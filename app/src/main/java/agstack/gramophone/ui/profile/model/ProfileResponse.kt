package agstack.gramophone.ui.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProfileResponse(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseProfileData,
    val gp_api_status: String
)




