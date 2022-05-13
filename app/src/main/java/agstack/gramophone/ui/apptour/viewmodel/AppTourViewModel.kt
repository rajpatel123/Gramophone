package agstack.gramophone.ui.apptour.viewmodel

import agstack.gramophone.Resource
import agstack.gramophone.ui.apptour.repository.AppTourRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class AppTourViewModel(private val appTourRepository: AppTourRepository) : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appTourRepository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}