package agstack.gramophone.ui.referandearn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GramcashFaqItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer")
	val answer: List<String?>? = null,
	var isExpanded :Boolean = false
) : Parcelable