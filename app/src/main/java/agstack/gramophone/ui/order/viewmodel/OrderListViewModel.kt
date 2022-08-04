package agstack.gramophone.ui.order.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.utils.Constants
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
    var recentOrderSize = MutableLiveData<Int>()
    var pastOrderSize = MutableLiveData<Int>()
    var emptyText = MutableLiveData<String>(getNavigator()?.getMessage(R.string.no_recent_order))
    var progress = MutableLiveData<Boolean>()

    init {
        selectedTab.value = 0
        recentOrderSize.value = 0
        pastOrderSize.value = 0
        progress.value = false
        emptyText.value = getNavigator()?.getMessage(R.string.no_recent_order)
    }

    fun onClickShopNow() {
        getNavigator()?.openHomeActivity()
    }

    fun getOrderList() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val recentOrderDataResponse = productRepository.getOrderData(Constants.RECENT)
                    val pastOrderDataResponse = productRepository.getOrderData(Constants.PAST)

                    if (recentOrderDataResponse.isSuccessful && recentOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && recentOrderDataResponse.body()?.gp_api_response_data?.order_list != null && recentOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size!! > 0
                    ) {
                        recentOrderSize.value = recentOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size
                        getNavigator()?.setRecentOrderAdapter(
                            OrderListAdapter(recentOrderDataResponse.body()?.gp_api_response_data?.order_list?.data!!)
                        ) {
                            getNavigator()?.openOrderDetailsActivity(Bundle().apply {
                                putString(Constants.ORDER_ID,
                                    it)
                            })
                        }
                    }

                    if (pastOrderDataResponse.isSuccessful && pastOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && pastOrderDataResponse.body()?.gp_api_response_data?.order_list != null && pastOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size!! > 0
                    ) {
                        pastOrderSize.value = pastOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size
                        getNavigator()?.setPastOrderAdapter(
                            OrderListAdapter(pastOrderDataResponse.body()?.gp_api_response_data?.order_list?.data!!)
                        ) {
                            getNavigator()?.openOrderDetailsActivity(Bundle().apply {
                                putString(Constants.ORDER_ID,
                                    it)
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

}
