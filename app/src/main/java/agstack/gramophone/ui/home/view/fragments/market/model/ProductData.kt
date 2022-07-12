package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@JsonInclude(JsonInclude.Include.NON_NULL)
@Parcelize
 data class ProductData(
    @SerializedName("product_id")
    var product_id:Int =0
): Parcelable