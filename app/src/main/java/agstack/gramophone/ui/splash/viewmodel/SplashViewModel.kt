package agstack.gramophone.ui.splash.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.utils.*
import android.content.res.Resources
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<SplashNavigator>() {

    var splashViewModel: MutableLiveData<ApiResponse<SplashModel>> = MutableLiveData()

    fun initSplash(resources: Resources) {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.logged_in) == true) {
            viewModelScope.launch {
                getNavigator()?.onLoading()
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val response = onBoardingRepository.getCustomerLanguage()
                        if (Constants.GP_API_STATUS.equals(response?.body()?.gp_api_status)) {
                            if (response.isSuccessful ) {
                                LocaleManagerClass.updateLocale(response.body()?.gp_api_response_data?.customer_lang, resources)
                            }
                            delay(3000)
                            updateLiveData()
                        } else {
                            getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))
                        }
                    } else
                        getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                    }
                }
            }
        }else{
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