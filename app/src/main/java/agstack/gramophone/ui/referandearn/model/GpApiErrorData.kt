package agstack.gramophone.ui.referandearn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiErrorData(
	val any: String? = null
) : Parcelable