package agstack.gramophone.ui.home.view.fragments.community.model.likes

data class DataX(
    val _id: String,
    val author: Author,
    val createdDate: Long,
    val postId: String,
    val following: Boolean
)