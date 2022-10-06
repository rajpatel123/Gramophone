package agstack.gramophone.ui.comments

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommentsAdapter

interface CommentNavigator: BaseNavigator {
    fun updateCommentsList(commentsAdapter: CommentsAdapter, onItemCommentsClicked: (commentId :String) -> Unit)
}