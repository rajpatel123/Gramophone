package agstack.gramophone.ui.postdetails

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.postdetails.model.Tag
import agstack.gramophone.widget.FilePicker

interface PostDetailNavigator :BaseNavigator {
    fun updatePostList(
        comments: CommentsAdapter,
        postDetailClicked: (commentId: String) -> Unit)
    fun sharePost(link: String)
     fun onImageSet(url: String)
     fun setLikeImage(icLiked: Int)
     fun setBookMarkImage(bookmark: Int)
    fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit)
    fun openCameraToCapture()
    fun clearImage()
    fun setTags(tags: List<Tag>)
}