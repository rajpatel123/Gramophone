package agstack.gramophone.splash.viewmodel

import agstack.gramophone.Resource
import agstack.gramophone.ui.login.repository.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class LoginViewModel(private val splashRepository: LoginRepository) : ViewModel() {

    fun loginUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = splashRepository.loginUser()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}