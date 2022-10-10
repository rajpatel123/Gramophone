package agstack.gramophone.ui.postdetails.model

data class LastComment(
    val __v: Int,
    val _id: String,
    val author: AuthorX,
    val createdDate: Long,
    val image: Any,
    val postId: String,
    val tags: List<Any>,
    val text: String
)