package agstack.gramophone.ui.createpost

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.createnewpost.view.PostDetailsModel
import agstack.gramophone.ui.createnewpost.view.model.GpApiResponseData
import agstack.gramophone.ui.tagandmention.Tag

interface CreatePostNavigator : BaseNavigator {
    fun openCameraToCapture()
    fun updateAddDeleteBtn(imageNo: Int)
    fun populateTagSuggestionList(tags: Array<Tag>)
    fun populateHasTagList(tags: Array<Tag>)
    fun enablePostButton()
    fun disablePostButton()
    fun gotoSocialPage(post: PostDetailsModel)
    fun populatePostDetails(postDetailsModel: PostDetailsModel)
}