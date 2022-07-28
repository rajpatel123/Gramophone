package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewList(

	@field:SerializedName("data")
	val data: List<ReviewListItem?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null


) : Parcelable