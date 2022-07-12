package agstack.gramophone.ui.home.view.fragments.market.model.test

import agstack.gramophone.ui.home.view.fragments.market.model.GpApiErrorData
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestClass(

	@field:SerializedName("gp_api_error_data")
	val gpApiErrorData: GpApiErrorData? = null
) : Parcelable