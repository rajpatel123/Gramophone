package agstack.gramophone.ui.home.view.fragments.market.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OffersProductResponseData(


    @field:SerializedName("gp_api_response_data")
    val gpApiResponseData: GpApiResponseDataOffersOnProduct? = null,

    @field:SerializedName("gp_api_status")
    val gpApiStatus: String? = null,

    @field:SerializedName("gp_api_message")
    val gpApiMessage: String? = null,

    ) : Parcelable