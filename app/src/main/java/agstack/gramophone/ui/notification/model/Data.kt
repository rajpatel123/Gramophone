package agstack.gramophone.ui.notification.model

data class Data(
    val content: Content,
    val created_at: String,
    val id: Int,
    val important: Boolean,
    val sent_by: Int,
    val viewed: Boolean
)