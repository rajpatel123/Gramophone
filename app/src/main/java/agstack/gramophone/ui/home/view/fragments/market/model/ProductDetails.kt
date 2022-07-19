package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetails(

	@field:SerializedName("Key Points")
	val keyPoints: List<KeyPointsItem?>? = null
) : Parcelable