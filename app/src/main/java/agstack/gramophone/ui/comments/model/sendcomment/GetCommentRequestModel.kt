package agstack.gramophone.ui.comments.model.sendcomment

data class GetCommentRequestModel(
    val postId:String,
    val limit:Int?
)
