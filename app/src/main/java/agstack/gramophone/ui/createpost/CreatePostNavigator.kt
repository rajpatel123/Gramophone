package agstack.gramophone.ui.createpost

import agstack.gramophone.base.BaseNavigator

interface CreatePostNavigator : BaseNavigator {
    fun openCameraToCapture()
    fun updateAddDeleteBtn(imageNo: Int)
}