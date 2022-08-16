package agstack.gramophone.ui.userprofile

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.widget.FilePicker

interface UserProfileNavigator: BaseNavigator {
    fun showImageSelect(file: FilePicker, onCamera: () -> Unit, onGallery: () -> Unit)
    fun openCameraToCapture()
}