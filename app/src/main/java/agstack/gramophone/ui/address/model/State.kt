package agstack.gramophone.ui.address.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("name")
    val name: String?,

    var selected: Boolean,
    @SerializedName("image")
    val image: String?,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<State> {
        override fun createFromParcel(parcel: Parcel): State {
            return State(parcel)
        }

        override fun newArray(size: Int): Array<State?> {
            return arrayOfNulls(size)
        }
    }

}