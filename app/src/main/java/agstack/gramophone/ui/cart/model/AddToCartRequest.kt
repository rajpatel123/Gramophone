package agstack.gramophone.ui.cart.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@JsonInclude(JsonInclude.Include.NON_NULL)
@Parcelize
data class AddToCartRequest(
    @SerializedName("product_id")
    var product_id: Int = 0,
    @SerializedName("quantity")
    var quantity: Int = 0,
) : Parcelable
