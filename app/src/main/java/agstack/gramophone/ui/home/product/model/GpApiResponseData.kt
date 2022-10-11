package agstack.gramophone.ui.home.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiResponseData(

	@field:SerializedName("promotion_applicable")
	val promotionApplicable: Boolean? = null
) : Parcelable