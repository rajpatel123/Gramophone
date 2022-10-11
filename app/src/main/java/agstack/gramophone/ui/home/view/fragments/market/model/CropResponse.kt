package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CropResponse(
    @SerializedName("gp_api_status") var gpApiStatus: String? = null,
    @SerializedName("gp_api_message") var gpApiMessage: String? = null,
    @SerializedName("gp_api_response_data") var gpApiResponseData: GpApiResponseCropData? = GpApiResponseCropData(),
) : Parcelable

@Parcelize
data class GpApiResponseCropData(
    @SerializedName("crops_list") var cropsList: ArrayList<CropData> = arrayListOf(),
) : Parcelable

@Parcelize
data class CropData(
    @SerializedName("crop_id") var cropId: Int? = null,
    @SerializedName("crop_name") var cropName: String? = null,
    @SerializedName("crop_image") var cropImage: String? = null,
    @SerializedName("is_section_header")  var isSectionHeader: Boolean = false,
    @SerializedName("section_title")  var sectionTitle: String? = null,
    @SerializedName("section_sub_title")  var sectionSubTitle: String? = null,
    @SerializedName("is_selected")  var isSelected: Boolean = false,
) : Parcelable