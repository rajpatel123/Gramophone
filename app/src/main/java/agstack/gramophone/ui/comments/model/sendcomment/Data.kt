package agstack.gramophone.ui.comments.model.sendcomment

data class Data(
    val __v: Int,
    val _id: String,
    val author: Author,
    val authorId: String,
    val complainCount: Int,
    val createdDate: Long,
    val image: Any,
    val postId: String,
    val status: Int,
    val tags: List<Any>,
    val text: String,
    val urlPreviewMeta: UrlPreviewMeta
)