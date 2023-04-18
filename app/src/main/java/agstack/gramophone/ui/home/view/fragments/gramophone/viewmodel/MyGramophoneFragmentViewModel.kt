package agstack.gramophone.ui.home.view.fragments.gramophone.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.farm.model.FarmRequest
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.gramophone.MyGramophoneFragmentNavigator
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import androidx.lifecycle.viewModelScope
import com.moengage.core.Properties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class MyGramophoneFragmentViewModel
@Inject constructor(
    private val productRepository: ProductRepository,
    private val settingsRepository: SettingsRepository,
    private val communityRepository: CommunityRepository,
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<MyGramophoneFragmentNavigator>() {

    fun onViewProfileClicked() {
        getNavigator()?.openActivity(UserProfileActivity::class.java, null)
        val properties = Properties()
        properties.addAttribute(
            "Customer_Id",
            SharedPreferencesHelper.instance?.getString(
                SharedPreferencesKeys.CUSTOMER_ID
            )!!)
            .setNonInteractive()
        getNavigator()?.sendMoEngageEvent("KA_Click_ViewProfile", properties)
    }

    fun initProfile() {
        getNavigator()?.setUserName()
        getNavigator()?.setUserAddress()
        getNavigator()?.setUserImage()
    }

    fun getGramCash() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val response = settingsRepository.getGramCash()
                        if (response.body() != null && Constants.GP_API_STATUS.equals(response?.body()!!.gpApiStatus)) {
                            getNavigator()?.updateGramCash(response.body()!!)
                        } else {
                            getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))

                        }

                    } else {
                        getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                    }
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                    }
                }
            }
        }
    }


    fun getMyPost() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val response = communityRepository.getCommunityPost(
                            CommunityRequestModel(
                                "self",
                                1,
                                1
                            )
                        )
                        if (response.body() != null) {
                            getNavigator()?.updateCommunity(response.body()!!)
                        } else {
                            getNavigator()?.onError(Utility.getErrorMessage(response.errorBody()))

                        }

                    } else {
                        getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                    }
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                    }
                }
            }
        }
    }

    fun likePost(_id: String) {
        viewModelScope.launch {

            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = communityRepository.likePost(PostRequestModel(_id, ""))
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        getNavigator()?.updateLike(data)
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

    fun getPlacedOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val placeOrderResponse =
                        productRepository.getOrderData(
                            Constants.PLACED,
                            1.toString(),
                            "1"
                        )
                    if (placeOrderResponse.isSuccessful && placeOrderResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && placeOrderResponse.body()?.gp_api_response_data?.data != null && placeOrderResponse.body()?.gp_api_response_data?.data?.size!! > 0
                    ) {
                        getNavigator()?.updateOrderSection(placeOrderResponse.body()?.gp_api_response_data,Constants.PLACED)
                    }else{
                        getRecentOrder()
                    }

                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
    fun getRecentOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val placeOrderResponse =
                        productRepository.getOrderData(
                            Constants.RECENT,
                            1.toString(),
                            "1"
                        )
                    if (placeOrderResponse.isSuccessful && placeOrderResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && placeOrderResponse.body()?.gp_api_response_data?.data != null && placeOrderResponse.body()?.gp_api_response_data?.data?.size!! > 0
                    ) {
                        getNavigator()?.updateOrderSection(placeOrderResponse.body()?.gp_api_response_data,Constants.RECENT)
                    }else{
                        getPastOrder()
                    }

                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }
    fun getPastOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val placeOrderResponse =
                        productRepository.getOrderData(
                            Constants.PAST,
                            1.toString(),
                            "1"
                        )
                    if (placeOrderResponse.isSuccessful && placeOrderResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && placeOrderResponse.body()?.gp_api_response_data?.data != null && placeOrderResponse.body()?.gp_api_response_data?.data?.size!! > 0
                    ) {
                        getNavigator()?.updateOrderSection(placeOrderResponse.body()?.gp_api_response_data,Constants.PAST)
                    }else{
                        getNavigator()?.updateOrderSection(null, Constants.PAST)
                    }

                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getFarms() {
        viewModelScope.launch {
            try {
                val response = productRepository.getFarmsData("active", FarmRequest("1", "1"))
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS && response.body()?.gp_api_response_data != null ) {
                    getNavigator()?.updateFarms(response.body()!!)
                }else{
                    getNavigator()?.updateFarms(null)
                }

            } catch (ex: Exception) {
                when (ex) {
                    /*is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))*/
                }
            }
        }
    }


    fun getMyGramophoneData() {
        viewModelScope.launch {
            try {
                val response = onBoardingRepository.getMyGramophoneData()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS && response.body()?.gp_api_response_data != null ) {
                    getNavigator()?.updateMyFavoriteSection(response.body()!!)
                }else{

                }

            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                   // else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getFavouritePostCount() {
        viewModelScope.launch {
            try {
                val response = communityRepository.getFavouriteCount()
                if (response.isSuccessful ) {
                   getNavigator()?.updateMyFavoritePostCount(response.body()!!.data.bookMarkedPostCounts)
                }else{

                }

            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

}