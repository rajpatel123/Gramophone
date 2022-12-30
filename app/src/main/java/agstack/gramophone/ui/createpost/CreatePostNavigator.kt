package agstack.gramophone.ui.createpost

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.createnewpost.model.PostDetailsModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.postdetails.model.Image
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
    fun showTags(selectedTagList: MutableList<CropData>)
    fun getPostDetails()
    fun loadImages(images: List<Image>)
    fun populateProblemList(tagArray: Array<Tag>)
    fun getText():String
}