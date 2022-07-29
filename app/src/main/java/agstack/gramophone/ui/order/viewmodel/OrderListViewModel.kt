package agstack.gramophone.ui.order.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<OrderListNavigator>() {

    fun getOrderList() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    getNavigator()?.onLoading()

                    coroutineScope {
                        val recentOrderData = async {
                            productRepository.getOrderData(Constants.RECENT)
                        }

                        val pastOrderData = async {
                            productRepository.getOrderData(Constants.PAST)
                        }

                        val recentOrderDataResponse = recentOrderData.await()
                        val pastOrderDataResponse = pastOrderData.await()

                        if (recentOrderDataResponse.isSuccessful && recentOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                            && recentOrderDataResponse.body()?.gp_api_response_data?.order_list != null && recentOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size!! > 0
                        ) {
                            getNavigator()?.setOrderAdapter(
                                OrderListAdapter(recentOrderDataResponse.body()?.gp_api_response_data?.order_list?.data!!)
                            ) {

                                getNavigator()?.openOrderDetailsActivity(Bundle().apply { putString(Constants.ORDER_ID, it) })
                            }
                        } else {

                        }

                        if (pastOrderDataResponse.isSuccessful && pastOrderDataResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                            && pastOrderDataResponse.body()?.gp_api_response_data?.order_list != null && pastOrderDataResponse.body()?.gp_api_response_data?.order_list?.data?.size!! > 0
                        ) {

                        } else {

                        }
                    }
                } else {
                    getNavigator()?.hideProgressBar()
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                getNavigator()?.hideProgressBar()
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

}
