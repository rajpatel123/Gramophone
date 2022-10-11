package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreResponse(
    @SerializedName("gp_api_status") var gpApiStatus: String? = null,
    @SerializedName("gp_api_message") var gpApiMessage: String? = null,
    @SerializedName("gp_api_response_data") var gpApiResponseData: GpApiResponseStoreData? = GpApiResponseStoreData(),
) : Parcelable

@Parcelize
data class GpApiResponseStoreData(
    @SerializedName("stores_list") var storeList: ArrayList<StoreData> = arrayListOf(),
) : Parcelable

@Parcelize
data class StoreData(
    @SerializedName("store_id") var storeId: Int? = null,
    @SerializedName("store_image") var storeImage: String? = null,
    @SerializedName("store_name") var storeName: String? = null,
    @SerializedName("highlight") var highlight: String? = null,
) : Parcelable