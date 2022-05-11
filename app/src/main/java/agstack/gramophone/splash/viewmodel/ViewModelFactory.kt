package agstack.gramophone.splash.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.language.repository.LanguageRepository
import agstack.gramophone.language.viewmodel.LanguageViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            return LanguageViewModel(LanguageRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

