package agstack.gramophone.ui.userprofile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserLocation(
    var lat: Double = 0.0,
    var lang: Double = 0.0
):Parcelable
