package agstack.gramophone.ui.address.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class GpApiResponseData(
    val state_list: ArrayList<State>?,
    val state_top_list: ArrayList<State>?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(State),
        parcel.createTypedArrayList(State)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(state_list)
        parcel.writeTypedList(state_top_list)
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