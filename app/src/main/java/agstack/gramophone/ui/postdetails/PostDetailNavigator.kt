package agstack.gramophone.ui.postdetails

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommentsAdapter

interface PostDetailNavigator :BaseNavigator {
    fun updatePostList(
        comments: CommentsAdapter,
        postDetailClicked: (commentId: String) -> Unit)
    fun sharePost(link: String)
     fun onImageSet(url: String)
     fun setLikeImage(icLiked: Int)
}