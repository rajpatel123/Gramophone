package agstack.gramophone.ui.farm.model

data class FarmResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace
)

data class GpApiErrorData(
    val error_code: String
)

data class GpApiResponseData(
    val customer_farm: CustomerFarm,
    val model_farm: ModelFarm
)

data class GpApiTrace(
    val gp_language: String,
    val gp_request_id: String,
    val gp_trace_id: String
)

data class CustomerFarm(
    val current_page: Int,
    val data: List<List<Data>>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: String,
    val to: Int,
    val total: Int
)

data class ModelFarm(
    val current_page: Int,
    val data: List<List<Data>>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: String,
    val to: Int,
    val total: Int
)

data class Data(
    val crop_id: Int,
    val crop_image: String,
    val crop_name: String,
    val crop_sowing_date: String,
    val days: String,
    val duration: String,
    val farm_area: String,
    val farm_ref_id: String,
    val stage_name: String
)

data class Link(
    val active: Boolean,
    val label: String,
    val url: String
)