package agstack.gramophone.ui.profile.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.ui.profile.repository.ProfileSelectionRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProfileSelectionViewModel::class.java)) {
            return ProfileSelectionViewModel(ProfileSelectionRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

