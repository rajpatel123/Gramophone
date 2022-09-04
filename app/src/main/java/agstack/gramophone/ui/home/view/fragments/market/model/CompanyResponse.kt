package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyResponse(
    @SerializedName("gp_api_status") var gpApiStatus: String? = null,
    @SerializedName("gp_api_message") var gpApiMessage: String? = null,
    @SerializedName("gp_api_response_data") var gpApiResponseData: GpApiResponseCompanyData? = GpApiResponseCompanyData(),
) : Parcelable

@Parcelize
data class GpApiResponseCompanyData(
    @SerializedName("companies_list") var companiesList: ArrayList<CompanyData> = arrayListOf(),
) : Parcelable

@Parcelize
data class CompanyData(
    @SerializedName("company_id") var companyId: Int? = null,
    @SerializedName("company_image") var companyImage: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
) : Parcelable