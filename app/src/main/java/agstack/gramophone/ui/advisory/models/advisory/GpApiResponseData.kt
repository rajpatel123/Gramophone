package agstack.gramophone.ui.advisory.models.advisory

data class GpApiResponseData(
    val activity: List<Activity>,
    val category_id: Int,
    val category_stage_id: Int,
    val crop_stage_images: List<String>,
    val is_current_stage: Boolean,
    val stage_description: String,
    val stage_end_day: Int,
    val stage_name: String,
    val stage_start_day: Int,
    var isSelected:Boolean= false
)