package agstack.gramophone.ui.postdetails.model

import com.google.gson.annotations.SerializedName

data class Data(
    val __v: Int,
    val _id: String,
    val author: Author,
    val commentsCount: Int,
    val complainCount: Int,
    val createdDate: String,
    val description: Any,
    val facebookShare: Int,
    val imagePath: Any,
    val images: List<Image>,
    val lastComment: LastComment,
    val lastLike: LastLike,
    val liked: Boolean,
    val likesCount: Int,
    val otherShare: Int,
    val pinned: Boolean,
    val postType: String,
    val status: Int,
    val tags: List<Tag>,
    val updatedAt: Long,
    val updatedBy: String,
    val urlPreviewMeta: UrlPreviewMeta,
    val whatsAppShare: Int,
    val bookMarked: Boolean,
    val linkUrl: String,
    val showingDate:String?
)