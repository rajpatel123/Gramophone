package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.res.Resources
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashNavigator>() {

    var splashViewModel: MutableLiveData<ApiResponse<SplashModel>> = MutableLiveData()

    fun initSplash(resources: Resources) {
        if (TextUtils.isEmpty(SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.languageCode))){
            LocaleManagerClass.updateLocale("en", resources)
        }else{
            LocaleManagerClass.updateLocale(SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.languageCode), resources)
        }

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