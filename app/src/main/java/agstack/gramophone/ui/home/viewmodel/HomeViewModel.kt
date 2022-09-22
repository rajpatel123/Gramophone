package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.feedback.FeedbackActivity
import agstack.gramophone.ui.gramcash.GramCashActivity
import agstack.gramophone.ui.offerslist.OffersListActivity
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.settings.view.SettingsActivity
import agstack.gramophone.ui.unitconverter.UnitConverterActivity
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.ui.weather.WeatherActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : BaseViewModel<HomeActivityNavigator>() {

    var progressBar = ObservableField<Boolean>()
    var profileName = MutableLiveData<String>()
    var profileMobile = MutableLiveData<String>()
    var profileImage = MutableLiveData<String>()

    fun logout(v: View) {
        logoutUser()
    }

    fun getProfile() {
        progressBar.set(true)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = onBoardingRepository.getProfile()
                    progressBar.set(false)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        val name = response.body()?.gp_api_response_data?.customer_name
                        val mobile = response.body()?.gp_api_response_data?.mobile_no
                        val image = response.body()?.gp_api_response_data?.profile_image
                        profileName.value = name!!
                        profileMobile.value = mobile!!
                        profileImage.value = image!!
                        getNavigator()?.setImageNameMobile(name, mobile, image)
                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.USERNAME,
                            name
                        )
                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.USER_MOBILE,
                            mobile
                        )
                        SharedPreferencesHelper.instance?.putString(
                            SharedPreferencesKeys.USER_IMAGE,
                            image
                        )
                    } else {
                        getNavigator()?.onError(response.message())
                    }


                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }

        }

    }

    private fun logoutUser() {
        progressBar.set(true)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val logoutResponse = onBoardingRepository.logoutUser()

                    val logoutResponseModel = handleResponse(logoutResponse).data

                    progressBar.set(false)

                    if (Constants.GP_API_STATUS.equals(logoutResponseModel?.gp_api_status)) {
                        SharedPreferencesHelper.instance?.putBoolean(
                            SharedPreferencesKeys.logged_in,
                            false
                        )
                        getNavigator()?.onSuccess(logoutResponseModel?.gp_api_message)
                        getNavigator()?.logout()

                    } else {
                        getNavigator()?.onError(logoutResponseModel?.gp_api_message)
                    }


                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }

        }

    }

    private fun handleResponse(logoutResponse: Response<LogoutResponseModel>): ApiResponse<LogoutResponseModel> {
        if (logoutResponse.isSuccessful) {
            logoutResponse.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(logoutResponse.message())
    }

    fun openOrderList() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(OrderListActivity::class.java, null)
    }

    fun openSettings() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(SettingsActivity::class.java, null)
    }

    fun openWeather() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(WeatherActivity::class.java, null)
    }

    fun shareApp() {
        getNavigator()?.shareApp(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain";
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            putExtra(Intent.EXTRA_SUBJECT, getNavigator()?.getMessage(R.string.share_app));
            putExtra(Intent.EXTRA_TEXT, getNavigator()?.getMessage(R.string.app_url));

        })
    }

    fun OpenUserProfile() {
        getNavigator()?.openActivity(UserProfileActivity::class.java, null)
    }

    fun openUnitConverter() {
        getNavigator()?.openActivity(UnitConverterActivity::class.java, null)
    }

    fun openLeaveFeedback() {
        getNavigator()?.openActivity(FeedbackActivity::class.java, null)
    }

    fun openOfferListClicked() {
        getNavigator()?.openActivity(OffersListActivity::class.java, null)
    }

    fun ReferandEarnClicked() {
        getNavigator()?.openActivity(ReferAndEarnActivity::class.java, null)
    }

    fun GramCashClicked(){
        getNavigator()?.openActivity(GramCashActivity::class.java , null)
    }
}