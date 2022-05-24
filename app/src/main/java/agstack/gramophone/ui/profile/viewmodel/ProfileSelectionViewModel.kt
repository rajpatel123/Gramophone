package agstack.gramophone.ui.profile.viewmodel

import agstack.gramophone.retrofit.Resource
import agstack.gramophone.ui.profile.repository.ProfileSelectionRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class ProfileSelectionViewModel(private val profileSelectionRepository: ProfileSelectionRepository) : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = profileSelectionRepository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}