package agstack.gramophone.ui.notification.model.cropdetails

data class GpApiResponseData(
    val crop_anticipated_completed_date: String,
    val crop_id: Int,
    val crop_image: String,
    val crop_name: String,
    val crop_sowing_date: String,
    val days: Int,
    val duration: Any,
    val farm_area: String,
    val farm_ref_id: String,
    val harvested_quantity: Any,
    val harvested_quantity_unit: Any,
    val id: Int,
    val is_crop_cycle_completed: Boolean,
    val stage_name: String
)