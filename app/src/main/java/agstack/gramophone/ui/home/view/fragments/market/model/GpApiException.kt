package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GpApiException(

	@field:SerializedName("trace")
	val trace: List<String?>? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("file_gp")
	val fileGp: String? = null,

	@field:SerializedName("file_line_gp")
	val fileLineGp: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("code_sample")
	val codeSample: CodeSample? = null
) : Parcelable