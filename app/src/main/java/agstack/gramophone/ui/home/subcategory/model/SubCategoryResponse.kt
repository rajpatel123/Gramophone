package agstack.gramophone.ui.home.subcategory.model

import agstack.gramophone.ui.home.view.fragments.market.model.CategoryData

data class SubCategoryResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

class GpApiErrorData

data class GpApiResponseData(
    val brands_list: List<Brands>,
    val crops_list: List<Crops>,
    val product_app_sub_categories_list: List<CategoryData>,
    val technical_data: List<TechnicalData>,
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String,
)

data class Brands(
    val brand_id: Int,
    val brand_name: String,
    var isChecked: Boolean = false
)

data class Crops(
    val crop_id: Int,
    val crop_name: String,
    var isChecked: Boolean = false
)

data class TechnicalData(
    val technical_code: Int,
    val technical_name: String,
    var isChecked: Boolean = false
)