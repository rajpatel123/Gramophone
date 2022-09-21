package agstack.gramophone.ui.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
): Parcelable