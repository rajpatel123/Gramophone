package agstack.gramophone.ui.createpost

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.tagandmention.Tag

interface CreatePostNavigator : BaseNavigator {
    fun openCameraToCapture()
    fun updateAddDeleteBtn(imageNo: Int)
    fun populateTagSuggestionList(tags: Array<Tag>)
}