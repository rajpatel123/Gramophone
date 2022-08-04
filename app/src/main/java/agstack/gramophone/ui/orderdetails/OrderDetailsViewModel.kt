package agstack.gramophone.ui.orderdetails

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<OrderDetailsNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var orderId = MutableLiveData<String>()
    var orderDate = MutableLiveData<String>()
    var quantity = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var orderStatus = MutableLiveData<String>()
    var paymentMethod = MutableLiveData<String>()
    var productSize = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()

    init {
        progress.value = false
        orderId.value = ""
        orderDate.value = ""
        quantity.value = ""
        price.value = ""
        orderStatus.value = ""
        paymentMethod.value = ""
        productSize.value = "0"
        username.value = ""
        address.value = ""
        mobile.value = ""
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.ORDER_ID)!! && bundle.getString(Constants.ORDER_ID) != null) {
            getOrderDetails(bundle.get(Constants.ORDER_ID) as String)
        }
    }

    private fun getOrderDetails(orderId: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val orderDetailRequest = OrderDetailRequest()
                    orderDetailRequest.order_id = orderId

                    val response = productRepository.getOrderDetails(orderDetailRequest)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.products != null && response.body()?.gp_api_response_data?.products?.size!! > 0
                    ) {
                        this@OrderDetailsViewModel.orderId.value =
                            response.body()?.gp_api_response_data?.order_id
                        this@OrderDetailsViewModel.orderDate.value =
                            response.body()?.gp_api_response_data?.order_date
                        this@OrderDetailsViewModel.quantity.value =
                            response.body()?.gp_api_response_data?.quantity
                        this@OrderDetailsViewModel.price.value =
                            response.body()?.gp_api_response_data?.price
                        this@OrderDetailsViewModel.orderStatus.value =
                            response.body()?.gp_api_response_data?.order_status
                        this@OrderDetailsViewModel.paymentMethod.value =
                            if (response.body()?.gp_api_response_data?.payment_method.equals("cod",
                                    true)
                            ) getNavigator()?.getMessage(R.string.cash_on_delivery)
                            else response.body()?.gp_api_response_data?.payment_method
                        this@OrderDetailsViewModel.productSize.value =
                            response.body()?.gp_api_response_data?.products?.size?.toString()
                        this@OrderDetailsViewModel.username.value =
                            response.body()?.gp_api_response_data?.products?.get(0)?.delivery_address?.name
                        this@OrderDetailsViewModel.address.value =
                            response.body()?.gp_api_response_data?.products?.get(0)?.delivery_address?.address
                        this@OrderDetailsViewModel.mobile.value =
                            response.body()?.gp_api_response_data?.products?.get(0)?.delivery_address?.mobile

                        getNavigator()?.setOrderListAdapter(
                            OrderedProductsAdapter(response.body()?.gp_api_response_data?.products!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity(ProductData(it.toInt()))
                            },
                            {
                                //on offer detail clicked
                                val promotionListItem = PromotionListItem()
                                promotionListItem.title = it.offer_name
                                promotionListItem.applicable_on_sku = it.valid_on_sku
                                promotionListItem.valid_till = it.valid_till
                                promotionListItem.product_name = it.product_name
                                promotionListItem.tnc = it.tnc
                                promotionListItem.redemption = it.redemption
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {
                                        putParcelable(Constants.OFFERSDATA, promotionListItem)

                                    })
                            })
                    } else {
                        getNavigator()?.showToast(response.message())
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
