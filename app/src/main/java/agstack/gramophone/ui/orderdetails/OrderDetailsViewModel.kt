package agstack.gramophone.ui.orderdetails

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.Address
import agstack.gramophone.ui.orderdetails.model.GpApiResponseData
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
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
    var subTotalPrice = MutableLiveData<Float>()
    var totalPrice = MutableLiveData<Float>()
    var orderStatus = MutableLiveData<String>()
    var orderStatusMessage = MutableLiveData<String>()
    var paymentMethod = MutableLiveData<String>()
    var productSize = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var discount = MutableLiveData<Float>()
    var gramCash = MutableLiveData<Int>()

    init {
        progress.value = false
        orderId.value = ""
        orderDate.value = ""
        quantity.value = ""
        subTotalPrice.value = 0f
        totalPrice.value = 0f
        orderStatus.value = ""
        orderStatusMessage.value = ""
        paymentMethod.value = ""
        productSize.value = "0"
        username.value = ""
        address.value = ""
        mobile.value = ""
        discount.value = 0f
        gramCash.value = 0
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.ORDER_ID)!! && bundle.getString(Constants.ORDER_ID) != null) {
            getOrderDetails(bundle.getString(Constants.ORDER_ID) as String,
                bundle.getString(Constants.ORDER_TYPE) as String)
        }
    }

    private fun getOrderDetails(orderId: String, orderType: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val orderDetailRequest = OrderDetailRequest()
                    orderDetailRequest.order_id = orderId
                    orderDetailRequest.type = orderType

                    val response = productRepository.getOrderDetails(orderDetailRequest)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS && response.body()?.gp_api_response_data != null
                        && response.body()?.gp_api_response_data?.products != null && response.body()?.gp_api_response_data?.products?.size!! > 0
                    ) {
                        val responseData: GpApiResponseData =
                            response.body()?.gp_api_response_data!!
                        this@OrderDetailsViewModel.orderId.value =
                            responseData.order_id.toString()
                        this@OrderDetailsViewModel.orderDate.value =
                            responseData.order_date
                        this@OrderDetailsViewModel.quantity.value =
                            responseData.items.toString()
                        this@OrderDetailsViewModel.orderStatus.value =
                            responseData.order_status
                        this@OrderDetailsViewModel.paymentMethod.value =
                            responseData.order_type
                        this@OrderDetailsViewModel.productSize.value =
                            responseData.products.size.toString()
                        this@OrderDetailsViewModel.username.value =
                            responseData.delivery_address.name
                        this@OrderDetailsViewModel.mobile.value =
                            responseData.delivery_address.mobile
                        val address: Address = responseData.delivery_address.address
                        if (address.isNotNull()) {
                            val fullAddress = StringBuilder("")
                            fullAddress.append(if (address.village.isNotNullOrEmpty()) address.village + ", " else "")
                            fullAddress.append(if (address.tehsil.isNotNullOrEmpty()) address.tehsil + ", " else "")
                            fullAddress.append(if (address.district.isNotNullOrEmpty()) address.district + ", " else "")
                            fullAddress.append(if (address.state.isNotNullOrEmpty()) address.state + ", " else "")
                            fullAddress.append(if (address.pincode.isNotNullOrEmpty()) address.pincode else "")

                            this@OrderDetailsViewModel.address.value = fullAddress.toString()
                        }
                        this@OrderDetailsViewModel.subTotalPrice.value =
                            responseData.pricing_details.sub_total_price
                        this@OrderDetailsViewModel.totalPrice.value =
                            responseData.pricing_details.total_price

                        val totalDiscount =
                            responseData.pricing_details.promotional_discount

                        this@OrderDetailsViewModel.discount.value = totalDiscount

                        this@OrderDetailsViewModel.gramCash.value =
                            responseData.pricing_details.gram_cash

                        if (responseData.vr_info.name.isNotNullOrEmpty() && orderStatus.value.equals(
                                "Order Dispatched")
                        ) {
                            val orderStatusMessage =
                                responseData.vr_info.name + ";<br>" + getNavigator()?.getMessage(R.string.phone) + " <font color = '#10203E'>" +
                                        if (responseData.vr_info.mobile_no.isNotNullOrEmpty()) {
                                            responseData.vr_info.mobile_no
                                        } else {
                                            getNavigator()?.getMessage(R.string.not_available)
                                        } + "</font></b> " + getNavigator()?.getMessage(R.string.order_has_been_assigned)
                            this@OrderDetailsViewModel.orderStatusMessage.value = orderStatusMessage
                        } else {
                            this@OrderDetailsViewModel.orderStatusMessage.value =
                                responseData.message
                        }

                        getNavigator()?.setColorOnOrderStatus(orderStatus.value!!)

                        getNavigator()?.setOrderListAdapter(
                            OrderedProductsAdapter(responseData.products),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity(ProductData(it.toInt()))
                            },
                            { offerApplied, productName, productSku ->
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {
                                        val offersDataItem = DataItem()
                                        offersDataItem.name = offerApplied.offer_name
                                        offersDataItem.endDate =
                                            if (offerApplied.valid_till.isNotNullOrEmpty()) offerApplied.valid_till.replace(
                                                "Valid till ",
                                                "") else null
                                        offersDataItem.productName = productName
                                        offersDataItem.productsku = productSku
                                        offersDataItem.image = offerApplied.image
                                        offersDataItem.termsConditions = offerApplied.t_c
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
