package agstack.gramophone.ui.home.view.fragments.community.model.quiz

data class Option(
    val answer: String,
    val option_id: String,
    val option_selected: Boolean,
    var question_id: String,
    val valid_answer: Boolean,
    var position: Int?,
    val votes_percent: Double?,


    )