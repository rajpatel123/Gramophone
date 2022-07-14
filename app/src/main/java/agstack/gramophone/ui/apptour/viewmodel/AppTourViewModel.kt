package agstack.gramophone.ui.apptour.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.apptour.AppTourNavigator
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppTourViewModel@Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<AppTourNavigator>() {

    fun onHelpClick(v: View){
        var initiateAppDataResponseModel = Gson().fromJson(
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.app_data
            ), InitiateAppDataResponseModel::class.java
        )
        getNavigator()?.onHelpClick(initiateAppDataResponseModel.gp_api_response_data.help_data_list.customer_support_no)
    }

    fun onLanguageClick(v: View){
        getNavigator()?.onLanguageChangeClick()

    }

}