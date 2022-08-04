package agstack.gramophone.ui.orderdetails.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@JsonInclude(JsonInclude.Include.NON_NULL)
@Parcelize
data class OrderDetailRequest(
    @SerializedName("order_id")
    var order_id: String = "",
) : Parcelable