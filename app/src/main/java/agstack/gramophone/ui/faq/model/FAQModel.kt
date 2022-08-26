package agstack.gramophone.ui.faq.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FAQModel(

    @field:SerializedName("questionId")
    val questionId: Int? = null,

    @field:SerializedName("question")
    val question: String? = null,

    @field:SerializedName("answer")
    val answer: String? = null,
    var isExpanded :Boolean = false
): Parcelable