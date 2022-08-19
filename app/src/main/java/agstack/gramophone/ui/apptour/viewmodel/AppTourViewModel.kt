package agstack.gramophone.ui.apptour.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.apptour.AppTourNavigator
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.Manifest
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AppTourViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<AppTourNavigator>() {
    private lateinit var initiateAppDataResponseModel: InitiateAppDataResponseModel
    private var scrollImagesJob: Job? = null
    var currentPage = 0


    fun startScroller() {
        scrollImagesJob = viewModelScope.launch {
            while (isActive) {
                delay(3_000)
                if (currentPage === initiateAppDataResponseModel?.gp_api_response_data?.login_banner_list?.size) {
                    currentPage = 0
                }
                getNavigator()?.updateImages(currentPage)

                currentPage++
            }

        }
        scrollImagesJob?.start()
    }

    fun onHelpClick(v: View) {
        var initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )
        if (getNavigator()?.requestPermission(Manifest.permission.CALL_PHONE) == true)
            getNavigator()?.onHelpClick(initiateAppDataResponseModel?.gp_api_response_data?.help_data_list?.customer_support_no!!)
    }

    fun onLanguageClick(v: View) {
        getNavigator()?.onLanguageChangeClick()

    }

    fun updateLanguage() = viewModelScope.launch {
        val updateLanguageRequestModel = UpdateLanguageRequestModel(getNavigator()?.getLanguage()!!)
        updateLanguage(updateLanguageRequestModel)
    }


    suspend fun updateLanguage(sendOtpRequestModel: UpdateLanguageRequestModel) {

        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.updateLanguageWhileOnBoarding(sendOtpRequestModel)
                val updateLanguageResponseModel = handleLanguageUpdateResponse(response).data

                if (Constants.GP_API_STATUS.equals(updateLanguageResponseModel?.gp_api_status)) {
                    getNavigator()?.showToast(updateLanguageResponseModel?.gp_api_message)
                    getNavigator()?.openAndFinishActivity(AppTourActivity::class.java,null)
                } else {
                    getNavigator()?.showToast(updateLanguageResponseModel?.gp_api_message)
                }

            } else
                getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet)!!)
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure)!!)
                else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    private fun handleLanguageUpdateResponse(response: Response<UpdateLanguageResponseModel>): ApiResponse<UpdateLanguageResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun setupViewPager() {
        initiateAppDataResponseModel =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
            )!!
        getNavigator()?.setupViewPager(initiateAppDataResponseModel?.gp_api_response_data?.login_banner_list)
    }


    fun moveToLogin() {
        scrollImagesJob?.cancel()
        getNavigator()?.openAndFinishActivity(LoginActivity::class.java, null)
    }

}