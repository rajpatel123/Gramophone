package agstack.gramophone.ui.cart.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.PlaceOrderRequest
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<CartNavigator>() {

    var itemCount = MutableLiveData<Int>()
    var discount = MutableLiveData<Float>()
    var gramCash = MutableLiveData<Int>()
    var applicableGramCash = MutableLiveData<Int>()
    var subTotal = MutableLiveData<String>()
    var totalAmount = MutableLiveData<Float>()
    var progress = MutableLiveData<Boolean>()
    var showGramCashCoinView = MutableLiveData<Boolean>()
    var showCartView = MutableLiveData<Boolean>()
    var isProgressBgTransparent = MutableLiveData<Boolean>()

    init {
        itemCount.value = 0
        discount.value = 0f
        gramCash.value = 0
        applicableGramCash.value = 0
        totalAmount.value = 0f
        subTotal.value = "0"
        progress.value = false
        showGramCashCoinView.value = false
        showCartView.value = true
        isProgressBgTransparent.value = false
    }

    fun onCheckedChange(button: CompoundButton, check: Boolean) {
        if (applicableGramCash.value!! > 0) {
            showGramCashCoinView.value = check
            if (check) {
                val total = totalAmount.value
                val gramCashCoin = applicableGramCash.value
                val amount = total!! - gramCashCoin!!
                totalAmount.value = amount
            } else {
                val total = totalAmount.value
                val gramCashCoin = applicableGramCash.value
                val amount = total!! + gramCashCoin!!
                totalAmount.value = amount
            }
        } else {
            if (check) {
                getNavigator()?.showToast(R.string.gram_cash_not_applicable)
                button.isChecked = false
                /*viewModelScope.launch {
                    delay(100)
                    button.isChecked = false
                }*/

            }
        }

    }

    fun onClickPlaceOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val placeOrderRequest = if (applicableGramCash.value!! > 0) {
                        PlaceOrderRequest(applicableGramCash.value.toString())
                    } else {
                        PlaceOrderRequest(null)
                    }

                    val response = productRepository.placeOrder(placeOrderRequest)
                    progress.value = false
                    getNavigator()?.showToast(response.body()?.gp_api_message)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getNavigator()?.openCheckoutStatusActivity(Bundle().apply {
                            putString(Constants.ORDER_ID,
                                response.body()?.gp_api_response_data?.order_ref_id.toString())
                        })
                        SharedPreferencesHelper.instance?.putInteger(
                            SharedPreferencesKeys.CART_ITEM_COUNT, 0)
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

    fun onClickStartShopping() {
        getNavigator()?.openHomeActivity()
    }

    fun getCartData() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.getCartData()

                    try {
                        if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                            && response.body()?.gp_api_response_data?.cart_items != null
                        ) {
                            SharedPreferencesHelper.instance?.putInteger(
                                SharedPreferencesKeys.CART_ITEM_COUNT,
                                response.body()?.gp_api_response_data?.cart_items!!.size)
                        } else {
                            SharedPreferencesHelper.instance?.putInteger(
                                SharedPreferencesKeys.CART_ITEM_COUNT,
                                0)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                    ) {
                        showCartView.value = true
                        isProgressBgTransparent.value = true
                        itemCount.value = response.body()?.gp_api_response_data?.cart_items?.size
                        discount.value = response.body()?.gp_api_response_data?.total_discount
                        subTotal.value = response.body()?.gp_api_response_data?.sub_total
                        totalAmount.value = response.body()?.gp_api_response_data?.total
                        gramCash.value = response.body()?.gp_api_response_data?.gramcash_coins

                        val applicableGramCashCoins: Int =
                            response.body()?.gp_api_response_data?.applicable_gramcash!!
                        val gramCashCoins: Int =
                            response.body()?.gp_api_response_data?.gramcash_coins!!

                        when {
                            applicableGramCashCoins < gramCashCoins -> {
                                applicableGramCash.value = applicableGramCashCoins
                            }
                            applicableGramCashCoins > gramCashCoins -> {
                                applicableGramCash.value = gramCashCoins
                            }
                            else -> {
                                applicableGramCash.value = applicableGramCashCoins
                            }
                        }

                        getNavigator()?.setCartAdapter(CartAdapter(response.body()?.gp_api_response_data?.cart_items!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity(ProductData(it.toInt()))
                            },
                            {
                                //on cartItem delete clicked
                                removeCartItem(it.toInt())
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
                            }, {
                                // on quantity increase and decrease clicked
                                val productData = ProductData()
                                productData.product_id = it.product_id.toInt()
                                productData.quantity = it.quantity
                                updateCart(productData)
                            })
                    } else {
                        showCartView.value = false
                    }
                    progress.value = false
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                showCartView.value = false
                progress.value = false
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun removeCartItem(productId: Int) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.removeCartItem(productId)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getCartData()
                    } else {
                        progress.value = false
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

    private fun updateCart(productData: ProductData) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.updateCartItem(productData)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getCartData()
                    } else {
                        progress.value = false
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
