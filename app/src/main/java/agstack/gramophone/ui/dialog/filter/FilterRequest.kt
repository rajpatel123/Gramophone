package agstack.gramophone.ui.dialog.filter


data class FilterRequest(
    val category_id: String,
    val sort_by: String,
    val limit: String,
    val page: String,
    val sub_category_ids: List<String>?=null,
    val brand_ids: List<String>?=null,
    val crop_ids: List<String>?=null,
    val technical_codes: List<String>?=null,
)