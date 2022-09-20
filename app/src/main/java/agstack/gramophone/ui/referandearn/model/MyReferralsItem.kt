package agstack.gramophone.ui.referandearn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyReferralsItem(

	@field:SerializedName("profile_image")
	val profileImage: String? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable