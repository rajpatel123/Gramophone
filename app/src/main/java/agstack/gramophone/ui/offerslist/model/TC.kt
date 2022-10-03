package agstack.gramophone.ui.offerslist.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TC(

	@field:SerializedName("hi")
	val hi: String? = null,

	@field:SerializedName("kn")
	val kn: String? = null,

	@field:SerializedName("mr")
	val mr: String? = null,

	@field:SerializedName("en")
	val en: String? = null
) : Parcelable