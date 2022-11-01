package agstack.gramophone.ui.home.view.fragments.community.model.quiz

data class Option(
    val answer: String,
    val option_id: String,
    val option_selected: Boolean,
    val question_id: String,
    val valid_answer: Boolean
)