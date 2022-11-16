package agstack.gramophone.ui.advisory.models.cropproblems.advisory

data class Activity(
    val activity_brief_description: String,
    val activity_id: Int,
    val activity_name: String,
    val created_at: String,
    val crop_id: Int,
    val id: Int,
    val linked_issues: List<LinkedIssue>,
    val linked_technicals: List<LinkedTechnical>,
    val short_application: String,
    val stage_id: Int,
    val status: Int,
    val updated_at: String
)