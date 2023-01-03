package agstack.gramophone.ui.postdetails

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.comments.model.Data
import agstack.gramophone.ui.home.adapter.CommentsAdapter
import agstack.gramophone.ui.postdetails.model.Tag
import agstack.gramophone.widget.FilePicker
import android.text.Spanned

interface PostDetailNavigator :BaseNavigator {
    fun updateCommentsList(commentsAdapter: CommentsAdapter,
                           onDeleteComment: (data : Data) -> Unit,
                           onEditCommentMenuClicked: (data : Data) -> Unit)
    fun sharePost(link: String)
     fun onImageSet(url: String)
     fun setLikeImage(icLiked: Int)
     fun setBookMarkImage(bookmark: Int)
    fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit)
    fun openCameraToCapture()
    fun clearImage()
    fun setTags(tags: List<Tag>)
    fun showBottomSheet()
    fun populateCommentData(data: Data)
    fun onLoadingTextMessage()
    fun setText(fromHtml: Spanned)
}