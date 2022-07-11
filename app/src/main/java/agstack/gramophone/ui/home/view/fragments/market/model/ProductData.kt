package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProductData(
    @SerializedName("product_id")
    var product_id: String? = null,
    @SerializedName("product_name")
    var product_name: String? = null,
    var product_images: List<String>? = null
): Parcelable