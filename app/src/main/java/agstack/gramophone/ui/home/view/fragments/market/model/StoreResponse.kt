package agstack.gramophone.ui.home.view.fragments.market.model

data class StoreResponse(
    val gp_api_error_data: GpApiErrorData,
    val gp_api_message: String,
    val gp_api_response_data: GpApiResponseStoreData,
    val gp_api_status: String,
    val gp_api_trace: GpApiTrace,
)

data class GpApiResponseStoreData(
    val stores_list: List<StoreData>,
)

data class StoreData(
    val store_id: Int,
    val store_image: String,
    val store_name: String,
)