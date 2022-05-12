package agstack.gramophone.splash.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.splash.repository.SplashRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(SplashRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

