package agstack.gramophone.ui.home.view.fragments.market.model

data class AllProductsResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: List<GpApiAllProductResponseData>,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

data class GpApiAllProductResponseData(
    val brand_id: Int,
    val company_id: Int,
    val currency: String,
    val highest_group_score: Double,
    val is_customer_favourite: Boolean,
    val product_app_category_ids: List<Int>,
    val product_app_details: List<ProductAppDetail>,
    val product_app_name: String,
    val product_base_name: String,
    val product_id_default: Int,
    val product_id_featured_default: Int,
    val product_images: List<String>,
    val product_sku_list: List<ProductSku>,
    val stores_ids: List<Int>,
    val technical_code: Int,
    val total_rating: Int,
    val youtube_video_id: String,
)

data class ProductAppDetail(
    val product_detail_key: String,
    val product_detail_type: String,
    val product_detail_value: String,
)

data class ProductSku(
    val app_featured_flag: Boolean,
    val is_gram_cash_applicable: Boolean,
    val mrp_price: Int,
    val product_id: Int,
    val product_sku_name: String,
    val region_id: Int,
    val sales_price: Int,
)