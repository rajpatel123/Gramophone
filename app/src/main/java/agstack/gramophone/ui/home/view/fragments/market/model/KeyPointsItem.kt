package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyPointsItem(

	@field:SerializedName("value")
	var value: String? = null,

	@field:SerializedName("key")
	var key: String? = null,

	@field:SerializedName("viewType")
	var viewType:String?="Normal"
) : Parcelable