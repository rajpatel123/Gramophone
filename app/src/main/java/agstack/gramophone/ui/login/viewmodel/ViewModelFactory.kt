package agstack.gramophone.ui.login.viewmodel

import agstack.gramophone.retrofit.ApiHelper
import agstack.gramophone.ui.login.repository.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

