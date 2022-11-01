package agstack.gramophone.ui.home.view.fragments.community.model.quiz

data class GpApiResponseData(
    val active: Boolean,
    val answered: Boolean,
    val image: String,
    val options: List<Option>,
    val question: String,
    val question_id: String,
    val question_type: String,
    val quiz_id: Int,
    val quiz_title: String,
    val quiz_type: String
)