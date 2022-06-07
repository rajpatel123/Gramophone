package agstack.gramophone.data.model

import android.os.Parcel

import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName


class UserResponse() : Parcelable {
    @SerializedName("page")
    var page = 0

    @SerializedName("results")
    var movies: List<User>? = null

    @SerializedName("total_results")
    var totalResults = 0

    @SerializedName("total_pages")
    var totalPages = 0

    constructor(parcel: Parcel) : this() {
        page = parcel.readInt()
        totalResults = parcel.readInt()
        totalPages = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(page)
        parcel.writeInt(totalResults)
        parcel.writeInt(totalPages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<UserResponse> {
        override fun createFromParcel(parcel: Parcel): UserResponse {
            return UserResponse(parcel)
        }

        override fun newArray(size: Int): Array<UserResponse?> {
            return arrayOfNulls(size)
        }
    }
}