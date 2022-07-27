package agstack.gramophone.ui.cart.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CartNavigator>() {

    private var amount: Int = 0
    private var totalAmount: Int = 0

    var cartCountLiveData = MutableLiveData<Int>()
    var amountLiveData = MutableLiveData<Int>()
    var discountLiveData = MutableLiveData<Int>()
    var gramCashLiveData = MutableLiveData<Int>()
    var totalAmountLiveData = MutableLiveData<Int>()

    init {
        cartCountLiveData.value = 0
        amountLiveData.value = 0
        discountLiveData.value = 0
        gramCashLiveData.value = 0
        totalAmountLiveData.value = 0
    }

    fun calculateAmount(cartItems: List<CartItem>) {
        amount = 0
        totalAmount = 0
        for (cartItem in cartItems) {
            amount += cartItem.price.toFloat().toInt() * cartItem.quantity.toInt()
        }
        totalAmount = amount - discountLiveData.value!! - gramCashLiveData.value!!

        amountLiveData.value = amount
        totalAmountLiveData.value = totalAmount
    }

    fun getCartData() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    getNavigator()?.onLoading()
                    val response = productRepository.getCartData()
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                    ) {
                        getNavigator()?.hideProgressBar()

                        cartCountLiveData.value = response.body()?.gp_api_response_data?.cart_items?.size
                        discountLiveData.value = response.body()?.gp_api_response_data?.total_discount
                        gramCashLiveData.value = response.body()?.gp_api_response_data?.gramcash_coins
                        calculateAmount(response.body()?.gp_api_response_data?.cart_items!!)

                        getNavigator()?.setCartAdapter(
                            CartAdapter(response.body()?.gp_api_response_data?.cart_items!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity()
                            },
                            {
                                //on cartItem delete clicked
                                getNavigator()?.deleteCartItem(it)
                                val productData = ProductData()
                                productData.product_id = it.toInt()
                                removeCartItem(productData)
                            },
                            {
                                //on offer clicked
                                getNavigator()?.openAppliedOfferDetailActivity(it)
                            })
                    } else {
                        getNavigator()?.hideProgressBar()
                        getNavigator()?.showToast(response.message())
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

    private fun removeCartItem(productData: ProductData) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    getNavigator()?.onLoading()
                    val response = productRepository.removeCartItem(productData)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getCartData()
                    }
                    getNavigator()?.showToast(response.body()?.gp_api_message)
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
