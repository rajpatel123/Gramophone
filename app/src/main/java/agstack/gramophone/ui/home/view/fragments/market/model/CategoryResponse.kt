package agstack.gramophone.ui.home.view.fragments.market.model

data class CategoryResponse(
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseCategoryData,
    val gp_api_status: String,
)

data class GpApiResponseCategoryData(
    val product_app_categories_list: List<CategoryData>
)

data class CategoryData(
    val category_id: Int,
    val category_image: String,
    val category_name: String,
    var isChecked: Boolean = false
)