package agstack.gramophone.ui.advisory.models.cropproblems

data class GpApiResponseData(
    val category_description: String,
    val category_id: Int,
    val category_image: String,
    val category_name: String,
    val category_type: String,
    val disease_id: Int,
    val nutrition_problem_id: Int,
    val stage_level: Int
)