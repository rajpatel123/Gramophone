package agstack.gramophone.ui.order.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.model.Data
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.Utility
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<OrderListNavigator>() {

    var selectedTab = MutableLiveData<Int>()
    var placedOrderSize = MutableLiveData<Int>()
    var recentOrderSize = MutableLiveData<Int>()
    var pastOrderSize = MutableLiveData<Int>()
    var orderDate = MutableLiveData<String>()
    var emptyText = MutableLiveData<String>(getNavigator()?.getMessage(R.string.no_recent_order))
    var progress = MutableLiveData<Boolean>()

    init {
        selectedTab.value = 0
        placedOrderSize.value = 0
        recentOrderSize.value = 1
        pastOrderSize.value = 1
        orderDate.value = ""
        progress.value = false
        emptyText.value = getNavigator()?.getMessage(R.string.no_recent_order)
    }

    fun onClickShopNow() {
        getNavigator()?.openHomeActivity()
    }

    fun getOrders() {
        getPlacedOrder()
        getRecentOrder()
        getPastOrder()
    }

    private fun getPlacedOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val placeOrderResponse = productRepository.getOrderData(
                        Constants.PLACED,
                        Constants.API_DATA_LIMITS_IN_ONE_TIME,
                        "1"
                    )
                    if (placeOrderResponse.isSuccessful && placeOrderResponse.body()?.gp_api_status == Constants.GP_API_STATUS && placeOrderResponse.body()?.gp_api_response_data?.data != null && placeOrderResponse.body()?.gp_api_response_data?.data?.size!! > 0) {
                        val placedList = ArrayList<Data>()
                        placedList.addAll(placeOrderResponse.body()?.gp_api_response_data?.data as ArrayList<Data>)
                        placedOrderSize.value = placedList.size

                        getNavigator()?.setPlacedOrderAdapter(
                            OrderListAdapter(placedList)
                        ) { orderId, price ->
                            getNavigator()?.openOrderDetailsActivity(Bundle().apply {
                                putString(Constants.ORDER_ID, orderId)
                                putString(Constants.ORDER_TYPE, Constants.PLACED)
                            })
                        }
                    }
                    progress.value = false
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun getRecentOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val recentOrderDataResponse = productRepository.getOrderData(
                        Constants.RECENT,
                        Constants.API_DATA_LIMITS_IN_ONE_TIME,
                        "1"
                    )
                    if (recentOrderDataResponse.isSuccessful && recentOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS && recentOrderDataResponse.body()?.gp_api_response_data?.data != null && recentOrderDataResponse.body()?.gp_api_response_data?.data?.size!! > 0) {
                        recentOrderSize.value =
                            recentOrderDataResponse.body()?.gp_api_response_data?.data?.size
                        getNavigator()?.setRecentOrderAdapter(
                            OrderListAdapter(recentOrderDataResponse.body()?.gp_api_response_data?.data!!)
                        ) { orderId, price ->
                            getNavigator()?.openOrderDetailsActivity(Bundle().apply {
                                putString(Constants.ORDER_ID, orderId)
                                putString(Constants.ORDER_TYPE, Constants.RECENT)
                            })
                        }
                    } else {
                        recentOrderSize.value = 0
                    }
                    progress.value = false
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                recentOrderSize.value = 0
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun getPastOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val pastOrderDataResponse = productRepository.getOrderData(
                        Constants.PAST,
                        Constants.API_DATA_LIMITS_IN_ONE_TIME,
                        "1"
                    )
                    if (pastOrderDataResponse.isSuccessful && pastOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS && pastOrderDataResponse.body()?.gp_api_response_data?.data != null && pastOrderDataResponse.body()?.gp_api_response_data?.data?.size!! > 0) {
                        pastOrderSize.value =
                            pastOrderDataResponse.body()?.gp_api_response_data?.data?.size
                        getNavigator()?.setPastOrderAdapter(
                            OrderListAdapter(pastOrderDataResponse.body()?.gp_api_response_data?.data!!)
                        ) { orderId, price ->
                            getNavigator()?.openOrderDetailsActivity(Bundle().apply {
                                putString(Constants.ORDER_ID, orderId)
                                putString(Constants.ORDER_TYPE, Constants.PAST)
                            })
                        }
                    } else {
                        pastOrderSize.value = 0
                    }
                    progress.value = false
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                pastOrderSize.value = 0
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun onHelpClicked() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    var producttoBeAdded = ProductData()
                    producttoBeAdded.product_id = null
                    producttoBeAdded.comments = ""

                    val helpResponse = productRepository.getHelp(Constants.HELP, producttoBeAdded,
                        SharedPreferencesHelper.instance?.getString(Constants.UTM_SOURCE),
                        SharedPreferencesHelper.instance?.getString(Constants.UTM_URL))

                    progress.value = false

                    if (helpResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {
                        getNavigator()?.showToast(helpResponse.body()?.gp_api_message)
                    } else {
                        getNavigator()?.showToast(Utility.getErrorMessage(helpResponse.errorBody()))
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }

    }

}
