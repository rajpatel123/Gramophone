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
import agstack.gramophone.ui.order.model.Data
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.lifecycle.viewModelScope
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
                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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
                                1000
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
                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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
                        val placedList = ArrayList<Data>()
                        placedList.addAll(placeOrderResponse.body()?.gp_api_response_data?.data as ArrayList<Data>)
                        getNavigator()?.updateOrderSection(placedList)
                    }else{
                        getNavigator()?.updateOrderSection(null)
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
                val response = productRepository.getFarmsData("active", FarmRequest("3", "1"))
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
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

}