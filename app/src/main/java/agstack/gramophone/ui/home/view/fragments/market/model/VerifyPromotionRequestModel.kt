package agstack.gramophone.ui.home.view.fragments.market.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class VerifyPromotionRequestModel(
    @SerializedName("product_id")
    var product_id: String,

    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("promotion_id")
    var promotion_id: String


) : Parcelable