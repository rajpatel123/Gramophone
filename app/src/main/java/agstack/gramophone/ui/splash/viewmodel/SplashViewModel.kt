package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.ui.login.repository.HomeRepository
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.utils.Resource
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var splashViewModel: MutableLiveData<Resource<SplashModel>> = MutableLiveData()

    fun getConfig() = viewModelScope.launch {
        getSystemConfig()
    }

    private suspend fun getSystemConfig() {
        delay(3000)
        splashViewModel.postValue(null)
    }

}