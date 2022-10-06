package agstack.gramophone.ui.home.subcategory.model

data class ApplicableOfferResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiOfferResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

data class GpApiOfferResponseData(
    val offers: List<Offer>,
)

data class Offer(
    val applicable_on_sku: String? = null,
    val amount_saved: Float,
    val end_date: String,
    val id: Int,
    val image: String,
    val product: Product,
    val product_base_name: String,
    val product_id: Int,
    val promotion_id: Int,
    val promotion_sub_type_id: Int,
    val promotion_type_id: Int,
    val t_c: String,
    val title: String,
    var selected: Boolean? = false,
)

data class Product(
    val product_base_name: String,
    val product_id: Int,
)