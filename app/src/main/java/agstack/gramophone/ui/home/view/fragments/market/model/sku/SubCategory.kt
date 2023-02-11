package agstack.gramophone.ui.home.view.fragments.market.model.sku

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubCategory(

    @field:SerializedName("category_id")
    val category_id: Int,

    @field:SerializedName("category_image")
    val category_image: String,

    @field:SerializedName("category_name")
    val category_name: String,

    @field:SerializedName("category_type")
    val category_type: String,

    @field:SerializedName("language")
    val language: String

):Parcelable