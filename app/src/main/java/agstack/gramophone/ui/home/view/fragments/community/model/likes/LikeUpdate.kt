package agstack.gramophone.ui.home.view.fragments.community.model.likes

data class LikeUpdate(
    val _id: String,
    val lastLike: LastLike,
    val liked: Boolean,
    val likesCount: Int
)