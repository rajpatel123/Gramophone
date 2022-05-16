package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.retrofit.Resource
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.ui.splash.repository.SplashRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashViewModel(private val splashRepository: SplashRepository) : ViewModel() {

    fun initSplashScreen() {
        viewModelScope.launch {
            delay(10000)
            updateLiveData()
        }
    }

    fun updateLiveData()= liveData(Dispatchers.IO) {
        val splashModel = SplashModel(true)
        emit(splashModel)
    }

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = splashRepository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}