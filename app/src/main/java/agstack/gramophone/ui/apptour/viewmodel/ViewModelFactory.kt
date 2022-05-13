package agstack.gramophone.ui.apptour.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.ui.apptour.repository.AppTourRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AppTourViewModel::class.java)) {
            return AppTourViewModel(AppTourRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

