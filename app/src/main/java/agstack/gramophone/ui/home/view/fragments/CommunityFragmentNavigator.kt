package agstack.gramophone.ui.home.view.fragments

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommunityPostAdapter

interface CommunityFragmentNavigator: BaseNavigator {
    fun updatePostList(
        postList: CommunityPostAdapter,
        postDetailClicked: (postId: String) -> Unit,
        onItemLikesClicked: (postId: String) -> Unit,
        onItemCommentsClicked: (postId: String) -> Unit,
        onWhatsAppClicked: (postId: String) -> Unit,
        onTripleDotMenuClicked: (postId:String) -> Unit,
        onMenuOptionClicked: (postId:String) -> Unit
    )

    fun openCommentDialog()
    fun sharePost(it: String)
    fun deletePostDialog()
    fun reportPostDialog()
    fun blockUserDialog()
}