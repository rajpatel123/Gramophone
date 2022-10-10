package agstack.gramophone.ui.orderdetails

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
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
    var subTotalPrice = MutableLiveData<String>()
    var totalPrice = MutableLiveData<String>()
    var orderStatus = MutableLiveData<String>()
    var orderStatusMessage = MutableLiveData<String>()
    var paymentMethod = MutableLiveData<String>()
    var productSize = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var discount = MutableLiveData<String>()
    var gramCash = MutableLiveData<String>()

    init {
        progress.value = false
        orderId.value = ""
        orderDate.value = ""
        quantity.value = ""
        subTotalPrice.value = ""
        totalPrice.value = ""
        orderStatus.value = ""
        orderStatusMessage.value = ""
        paymentMethod.value = ""
        productSize.value = "0"
        username.value = ""
        address.value = ""
        mobile.value = ""
        discount.value = "0"
        gramCash.value = "0"
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
                            response.body()?.gp_api_response_data?.order_id.toString()
                        this@OrderDetailsViewModel.orderDate.value =
                            response.body()?.gp_api_response_data?.order_date
                        this@OrderDetailsViewModel.quantity.value =
                            response.body()?.gp_api_response_data?.items.toString()
                        this@OrderDetailsViewModel.orderStatus.value =
                            response.body()?.gp_api_response_data?.order_status
                        this@OrderDetailsViewModel.orderStatusMessage.value =
                            response.body()?.gp_api_response_data?.message
                        this@OrderDetailsViewModel.paymentMethod.value =
                            response.body()?.gp_api_response_data?.order_type
                        this@OrderDetailsViewModel.productSize.value =
                            response.body()?.gp_api_response_data?.products?.size?.toString()
                        this@OrderDetailsViewModel.username.value =
                            response.body()?.gp_api_response_data?.delivery_address?.name
                        this@OrderDetailsViewModel.address.value =
                            response.body()?.gp_api_response_data?.delivery_address?.address
                        this@OrderDetailsViewModel.mobile.value =
                            response.body()?.gp_api_response_data?.delivery_address?.mobile
                        this@OrderDetailsViewModel.subTotalPrice.value =
                            response.body()?.gp_api_response_data?.pricing_details?.sub_total_price.toString()
                        this@OrderDetailsViewModel.totalPrice.value =
                            response.body()?.gp_api_response_data?.pricing_details?.total_price.toString()

                        val totalDiscount =
                            response.body()?.gp_api_response_data?.pricing_details?.product_discount!! +
                                    response.body()?.gp_api_response_data?.pricing_details?.promotional_discount!! +
                                    response.body()?.gp_api_response_data?.pricing_details?.coupon_discount!! +
                                    response.body()?.gp_api_response_data?.pricing_details?.additional_discount!!

                        this@OrderDetailsViewModel.discount.value = totalDiscount.toString()

                        this@OrderDetailsViewModel.gramCash.value =
                            response.body()?.gp_api_response_data?.pricing_details?.gram_cash.toString()

                        getNavigator()?.setColorOnOrderStatus(orderStatus.value!!)

                        getNavigator()?.setOrderListAdapter(
                            OrderedProductsAdapter(response.body()?.gp_api_response_data?.products!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity(ProductData(it.toInt()))
                            },
                            { freeProduct, productSku ->

                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {

                                        val offersDataItem = DataItem()
                                        offersDataItem.endDate = ""
                                        offersDataItem.productName = freeProduct.product_name
                                        offersDataItem.productsku = productSku
                                        offersDataItem.image = freeProduct.product_image
                                        offersDataItem.termsConditions = ""
                                        putParcelable(Constants.OFFERSDATA, offersDataItem)

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
