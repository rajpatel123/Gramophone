package agstack.gramophone.ui.address.model

import android.os.Parcel
import android.os.Parcelable

data class StateResponseModel(
    val gp_api_message: String?,
    val gp_api_response_data: GpApiResponseData?,
    val gp_api_status: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(GpApiResponseData::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gp_api_message)
        parcel.writeParcelable(gp_api_response_data, flags)
        parcel.writeString(gp_api_status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StateResponseModel> {
        override fun createFromParcel(parcel: Parcel): StateResponseModel {
            return StateResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<StateResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}