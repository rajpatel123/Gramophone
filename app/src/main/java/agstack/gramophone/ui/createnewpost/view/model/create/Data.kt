package agstack.gramophone.ui.createnewpost.view.model.create

data class Data(
    val __v: Int,
    val _id: String,
    val actionTimeStamp: Long,
    val author: Author,
    val authorId: String,
    val commentsCount: Int,
    val complainCount: Int,
    val createdDate: Long,
    val createdDateText: String,
    val description: String,
    val facebookShare: Int,
    val farmArea: FarmArea,
    val imagePath: Any,
    val images: List<Image>,
    val lastComment: LastComment,
    val lastLike: LastLike,
    val likesCount: Int,
    val linkUrl: String,
    val otherShare: Int,
    val pinned: Boolean,
    val showingDate: Any,
    val status: Int,
    val tags: List<Any>,
    val urlPreviewMeta: Any,
    val whatsAppShare: Int
)