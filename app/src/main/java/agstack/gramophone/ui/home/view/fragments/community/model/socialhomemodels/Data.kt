package agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels

data class Data(
    val __v: Int,
    val _id: String,
    val author: Author,
    var bookMarked: Boolean,
    val commentsCount: Int,
    val complainCount: Int,
    val createdDate: Long,
    val description: String,
    val facebookShare: Int,
    val hasComplain: Boolean,
    val imagePath: Any,
    val images: List<Image>,
    val lastComment: LastComment,
    val lastLike: LastLike,
    var liked: Boolean,
    var likesCount: Int,
    val otherShare: Int,
    val pinned: Boolean,
    val postType: String,
    val showingDate: Any,
    val status: Int,
    val tags: List<Tag>,
    val updatedAt: Long,
    val updatedBy: String,
    val urlPreviewMeta: UrlPreviewMeta,
    val whatsAppShare: Int,
    var isSelected: Boolean,
    var position:Int?

)