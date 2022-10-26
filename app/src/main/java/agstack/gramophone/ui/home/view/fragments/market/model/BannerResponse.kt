package agstack.gramophone.ui.home.view.fragments.market.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BannerResponse(
    @SerializedName("gp_api_status") var gpApiStatus: String? = null,
    @SerializedName("gp_api_message") var gpApiMessage: String? = null,
    @SerializedName("gp_api_response_data") var gpApiResponseData: GpApiResponseBannerData? = GpApiResponseBannerData(),
) : Parcelable

@Parcelize
data class GpApiResponseBannerData(
    @SerializedName("home_banner_1") var homeBanner1: ArrayList<Banner> = arrayListOf(),
    @SerializedName("home_gramophone_exclusive") var homeGramophoneExclusive: ArrayList<Banner> = arrayListOf(),
    @SerializedName("home_referral_banner") var homeReferralBanner: ArrayList<Banner> = arrayListOf(),
    @SerializedName("community") var communityBanner: ArrayList<Banner> = arrayListOf(),
    @SerializedName("my_farm") var myFarm: ArrayList<Banner> = arrayListOf(),
    @SerializedName("shop_by_store") var shopByStore: ArrayList<Banner> = arrayListOf(),
) : Parcelable

@Parcelize
data class Banner(
    @SerializedName("banner_type") var bannerType: String? = null,
    @SerializedName("banner_image") var bannerImage: String? = null,
    @SerializedName("banner_link") var bannerLink: String? = null,
    @SerializedName("banner_type_display") var bannerTypeDisplay: String? = null,
    @SerializedName("banner_type_screen") var bannerTypeScreen: String? = null,
) : Parcelable