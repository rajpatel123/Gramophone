package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashNavigator>() {

    var splashViewModel: MutableLiveData<Resource<SplashModel>> = MutableLiveData()


    fun getConfig() = viewModelScope.launch {
        getSystemConfig()
    }

    private suspend fun getSystemConfig() {
        delay(3000)
        splashViewModel.postValue(null)
    }

    fun initSplash() {
        viewModelScope.launch {
            delay(3000)
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.logged_in) == true){
            getNavigator()?.moveTOHome()
        }else{
            getNavigator()?.moveToLogIn()
        }

    }

}