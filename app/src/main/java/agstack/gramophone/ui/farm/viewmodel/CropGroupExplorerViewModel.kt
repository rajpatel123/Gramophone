package agstack.gramophone.ui.farm.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.farm.navigator.CropGroupExplorerNavigator
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CropGroupExplorerViewModel @Inject constructor() : BaseViewModel<CropGroupExplorerNavigator>() {
    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }
}