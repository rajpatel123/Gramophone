package agstack.gramophone.ui.home.subcategory.model

data class SubCategoryResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

class GpApiErrorData

data class GpApiResponseData(
    val product_app_sub_categories_list: List<ProductAppSubCategories>
)

data class GpApiTrace(
    val gp_request_id: String,
    val gp_trace_id: String
)

data class ProductAppSubCategories(
    val category_id: Int,
    val category_image: String,
    val category_name: String
)