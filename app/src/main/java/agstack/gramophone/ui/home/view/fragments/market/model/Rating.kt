package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rating(

	@field:SerializedName("total_review")
	val totalReview: Int? = null,

	@field:SerializedName("total_rating")
	val totalRating: Double? = null,

	@field:SerializedName("rating_count")
	val ratingCount: RatingCount? = null
) : Parcelable