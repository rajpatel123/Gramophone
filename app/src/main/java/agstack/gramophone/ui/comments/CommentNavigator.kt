package agstack.gramophone.ui.comments

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.widget.FilePicker

interface CommentNavigator: BaseNavigator {
    fun updateCommentsList(commentsAdapter: CommentsAdapter, onItemCommentsClicked: (commentId :String) -> Unit)
    fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit)
    fun openCameraToCapture()
    fun clearImage()
}