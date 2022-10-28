package agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels

data class BlockUserRequestModel(
    val action: String,
    val authorId: String,
    val postId: String?
)