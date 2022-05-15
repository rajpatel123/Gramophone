package agstack.gramophone.ui.verifyotp.viewmodel

import agstack.gramophone.retrofit.Resource
import agstack.gramophone.ui.verifyotp.repository.VerifyOtpRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class VerifyOtpViewModel(private val verifyOtpRepository: VerifyOtpRepository) : ViewModel() {

    fun loginUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = verifyOtpRepository.loginUser()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}