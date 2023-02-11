package agstack.gramophone.ui.farm.model.addfarm

data class GpApiResponseData(
    val area: String,
    val crop_anticipated_completed_date: String,
    val crop_id: String,
    val crop_sowing_date: String,
    val farm_id: Int,
    val status: String,
    val unit: String
)