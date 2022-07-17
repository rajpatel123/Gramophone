package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("self_rating")
	val selfRating: SelfRating? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("review_list")
	val reviewList: ReviewList? = null
) : Parcelable