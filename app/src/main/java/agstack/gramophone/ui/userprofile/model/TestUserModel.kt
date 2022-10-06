package agstack.gramophone.ui.userprofile.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestUserModel(

	@field:SerializedName("data")
	val data: Data? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("handle")
	val handle: String? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("phoneNo")
	val phoneNo: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("badge")
	val badge: String? = null,

	@field:SerializedName("location_name")
	val locationName: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: Long? = null,

	@field:SerializedName("totalFollowees")
	val totalFollowees: Int? = null,

	@field:SerializedName("firebaseId")
	val firebaseId: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("location")
	val location: UserLocation? = null,

	@field:SerializedName("totalFollowers")
	val totalFollowers: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("androidId")
	val androidId: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: Long? = null
) : Parcelable
