package agstack.gramophone.ui.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProfileResponse(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseProfileData,
    val gp_api_status: String,
)

@Parcelize
data class GpApiResponseProfileData(
    val address_data: UserAddress? = null,
    val customer_name: String? = null,
    val first_name:String? = null,
    val last_name : String? = null,
    val mobile_no: String? = null,
    val firm_name: String? = null,
    val is_farmer: Boolean? = null,
    val is_trader: Boolean? = null,
    val profile_image: String? = null,
    val username: String? = null
):Parcelable


@Parcelize
data class UserAddress(
    val master_address_id:String? = null,
    val state:String? = null,
    val district:String? = null,
    val tehsil:String? = null,
    val village:String? = null,
    val pincode:String? = null,
    val address:String? = null

):Parcelable
