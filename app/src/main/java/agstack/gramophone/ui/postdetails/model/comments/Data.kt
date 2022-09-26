package agstack.gramophone.ui.postdetails.model.comments

data class Data(
    val __v: Int,
    val _id: String,
    val author: Author,
    val createdDate: Long,
    val image: Any,
    val postId: String,
    val tags: List<Any>,
    val text: String,
    val commentDuration: String
)