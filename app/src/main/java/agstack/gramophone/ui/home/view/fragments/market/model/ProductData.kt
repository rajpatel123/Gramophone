package agstack.gramophone.ui.home.view.fragments.market.model

import agstack.gramophone.ui.language.model.Data
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProductData(
    @SerializedName("product_id")
    var product_id: String? = null,
    @SerializedName("product_name")
    var product_name: String? = null
): Parcelable