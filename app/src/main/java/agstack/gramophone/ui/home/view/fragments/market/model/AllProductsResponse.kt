package agstack.gramophone.ui.home.view.fragments.market.model

data class AllProductsResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiProductResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

data class GpApiProductResponseData(
    val current_page: Int,
    val data: List<Data>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int,
)

data class Data(
    val currency: String,
    val mrp_price: Float,
    val product_app_name: String,
    val product_id: Int,
    val product_image: String,
    val sales_price: Float,
)

data class Link(
    val active: Boolean,
    val label: String,
    val url: String,
)