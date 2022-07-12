package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiErrorData(

	@field:SerializedName("mobile_no")
	val mobileNo: List<String?>? = null
) : Parcelable