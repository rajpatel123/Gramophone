package agstack.gramophone.ui.home.viewmodel

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.articles.ArticlesWebViewActivity
import agstack.gramophone.ui.farm.view.ViewAllFarmsActivity

import agstack.gramophone.ui.feedback.FeedbackActivity
import agstack.gramophone.ui.gramcash.GramCashActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.model.FCMRegistrationModel
import agstack.gramophone.ui.offerslist.OffersListActivity
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.settings.view.SettingsActivity
import agstack.gramophone.ui.tv.GramophoneTVActivity
import agstack.gramophone.ui.unitconverter.UnitConverterActivity
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.ui.weather.WeatherActivity
import agstack.gramophone.utils.*
import agstack.gramophone.view.activity.CreatePostActivity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNull
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
    private val productRepository: ProductRepository,
) : BaseViewModel<HomeActivityNavigator>() {

    var progressBar = ObservableField<Boolean>()
    lateinit var mAlertDialog: AlertDialog

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
                    if (response.isNotNull() && response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        if (response.body()
                                .isNotNull() && response.body()?.gp_api_response_data.isNotNull()
                        ) {
                            val gpApiResponseData: GpApiResponseProfileData =
                                response.body()?.gp_api_response_data!!
                            if (gpApiResponseData.isNotNull()) {
                                val name =
                                    if (gpApiResponseData.customer_name.isNullOrEmpty()) "" else gpApiResponseData.customer_name
                                val mobile =
                                    if (gpApiResponseData.mobile_no.isNullOrEmpty()) "" else gpApiResponseData.mobile_no
                                val image =
                                    if (gpApiResponseData.profile_image.isNullOrEmpty()) "" else gpApiResponseData.profile_image
                                val gramCash =
                                    if (gpApiResponseData.gramcashpoints.isNull()) "" else gpApiResponseData.gramcashpoints.toString()
                                val customerId =
                                    if (gpApiResponseData.customer_id.isNullOrEmpty()) "" else gpApiResponseData.customer_id
                                val customerAddress =
                                    if (gpApiResponseData.address_data == null) "" else (gpApiResponseData.address_data.district.plus(
                                        ", "
                                    ).plus(gpApiResponseData.address_data.state))

                                SharedPreferencesHelper.instance?.putParcelable(
                                    SharedPreferencesKeys.PROFILE_DATA,
                                    gpApiResponseData
                                )

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

                                SharedPreferencesHelper.instance?.putString(
                                    SharedPreferencesKeys.CUSTOMER_ID,
                                    customerId
                                )

                                SharedPreferencesHelper.instance?.putString(
                                    SharedPreferencesKeys.CUSTOMER_ADDRESS,
                                    customerAddress
                                )

                                SharedPreferencesHelper.instance?.putParcelable(
                                    SharedPreferencesKeys.CUSTOMER_DATA,
                                    gpApiResponseData
                                )

                                getNavigator()?.setImageNameMobile(
                                    name,
                                    mobile,
                                    image,
                                    gramCash
                                )
                            }
                        }
                    } else {
                        getNavigator()?.onError(response.message())
                    }


                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
//                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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

                        SharedPreferencesHelper.instance?.clear()
                        getNavigator()?.logout()

                    } else {
                        getNavigator()?.onError(logoutResponseModel?.gp_api_message)
                    }


                } else{
                    progressBar.set(false)
                    getNavigator()?.openActivity(LostConnectionActivity::class.java)
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
//                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }

        }

    }

    fun getCrops() {
        try {
            viewModelScope.launch {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = productRepository.getCrops()
                    if (response.isSuccessful) {
                        SharedPreferencesHelper.instance?.putParcelable(
                            SharedPreferencesKeys.CROPS,
                            response.body()!!
                        )
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                }
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
//                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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

    fun openCropsNFarms() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(ViewAllFarmsActivity::class.java, Bundle().apply {
            putString(Constants.REDIRECTION_SOURCE, "Home")
        })
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

    fun openArticlesClicked() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
            putString(Constants.PAGE_URL, BuildConfig.BASE_URL_ARTICLES)
        })
    }

    fun openGramophoneTVClicked() {
        getNavigator()?.closeDrawer()
        getNavigator()?.openActivity(GramophoneTVActivity::class.java, null)
    }

    fun ReferandEarnClicked() {
        getNavigator()?.openActivity(ReferAndEarnActivity::class.java, null)
    }

    fun GramCashClicked() {
        getNavigator()?.openActivity(GramCashActivity::class.java, null)
    }

    fun onCreatePostClicked() {
        val properties = com.moengage.core.Properties()
        properties.addAttribute(
            "Customer_Id",
            SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!
        ).addAttribute("Redirection_Source", "Home Page").setNonInteractive()

        getNavigator()?.sendMoEngageEvent("KA_Open_CreatePost", properties)

        getNavigator()?.openActivity(CreatePostActivity::class.java, null)
    }

    fun onCallClicked() {
        if (getNavigator()?.isNetworkAvailable() == true){
            viewModelScope.launch {
                var producttoBeAdded = ProductData()
                producttoBeAdded.product_id = null

                val expertAdviceResponse =
                    productRepository.getHelp(Constants.HELP, producttoBeAdded,SharedPreferencesHelper.instance?.getString(Constants.UTM_SOURCE),SharedPreferencesHelper.instance?.getString(Constants.UTM_URL))

                if (expertAdviceResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                    getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                } else {
                    getNavigator()?.showToast(expertAdviceResponse.body()?.gp_api_message)
                }
            }
        }else{
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_connection))
        }
    }

    fun sendFCMToServer(fcmRegistrationModel: FCMRegistrationModel) {
        try {
            viewModelScope.launch {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = onBoardingRepository.saveToken(fcmRegistrationModel)
                    if (response.isSuccessful) {

                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(response.errorBody()))
                    }
                }
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
//                else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
            }
        }
    }

    fun openSetting() {
        getNavigator()?.openNotificationSetting()
    }

    fun cancel() {
        SharedPreferencesHelper.instance?.putBoolean(Constants.PUSH_ASKED, true)
        mAlertDialog?.dismiss()
    }

    fun setDialog(mAlertDialog: AlertDialog?) {
        this.mAlertDialog = mAlertDialog!!

    }

    fun retryBtn() {
        if (getNavigator()?.isNetworkAvailable() == true){
            getNavigator()?.finishActivity()
        }else{
            getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_connection))
        }
    }

}