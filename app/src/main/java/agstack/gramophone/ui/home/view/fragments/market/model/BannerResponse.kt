package agstack.gramophone.ui.home.view.fragments.market.model

data class BannerResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseBannerData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseBannerData(
    val home_banner_1: List<Banner>,
    val home_gramophone_exclusive: List<Banner>,
    val home_referral_banner: List<Banner>
)

data class GpApiTrace(
    val gp_request_id: String,
    val gp_trace_id: String
)

data class Banner(
    val banner_image: String,
    val banner_link: String,
    val banner_type: String,
    val banner_type_display: String,
    val banner_type_screen: String
)