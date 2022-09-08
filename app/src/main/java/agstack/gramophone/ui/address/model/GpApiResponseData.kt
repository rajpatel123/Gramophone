package agstack.gramophone.ui.address.model

import agstack.gramophone.ui.address.model.addressdetails.PinCode
import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class GpApiResponseData(
    val state_list: ArrayList<State>?,
    val state_top_list: ArrayList<State>?,
    val district_list: ArrayList<State>?,
    val tehsil_list: ArrayList<State>?,
    val village_list: ArrayList<State>?,
    val pincode_list: ArrayList<State>?,
    val district: String?,
    val state: String?,
    val tehsil: String?,
    val village: String?,
    val type: String?,
    val pincode: String?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(state_list)
        parcel.writeTypedList(state_top_list)
        parcel.writeTypedList(district_list)
        parcel.writeTypedList(tehsil_list)
        parcel.writeTypedList(village_list)
        parcel.writeTypedList(pincode_list)
        parcel.writeString(district)
        parcel.writeString(state)
        parcel.writeString(tehsil)
        parcel.writeString(village)
        parcel.writeString(type)
        parcel.writeString(pincode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GpApiResponseData> {
        override fun createFromParcel(parcel: Parcel): GpApiResponseData {
            return GpApiResponseData(parcel)
        }

        override fun newArray(size: Int): Array<GpApiResponseData?> {
            return arrayOfNulls(size)
        }
    }
}