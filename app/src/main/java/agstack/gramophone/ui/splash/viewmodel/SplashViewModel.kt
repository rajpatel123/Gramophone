package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.utils.Resource
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel<SplashNavigator>() {

    var splashViewModel: MutableLiveData<Resource<SplashModel>> = MutableLiveData()


    fun getConfig() = viewModelScope.launch {
        getSystemConfig()
    }

    private suspend fun getSystemConfig() {
        delay(3000)
        splashViewModel.postValue(null)
    }

    fun initSplash() {
        viewModelScope.launch {
            delay(500)
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        val splashModel = SplashModel(true)
        splashViewModel.postValue(Resource.Success(splashModel))
    }

}