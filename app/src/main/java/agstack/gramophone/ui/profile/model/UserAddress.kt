package agstack.gramophone.ui.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserAddress(
    val master_address_id:String? = null,
    val state:String? = null,
    val district:String? = null,
    val tehsil:String? = null,
    val village:String? = null,
    val pincode:String? = null,
    val address:String? = null

): Parcelable
