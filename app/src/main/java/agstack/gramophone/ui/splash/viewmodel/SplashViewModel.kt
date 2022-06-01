package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.ui.splash.model.SplashModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashViewModel() : ViewModel() {

    var liveData: MutableLiveData<SplashModel> = MutableLiveData()


}