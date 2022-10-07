package agstack.gramophone.ui.cart.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.widget.CompoundButton
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

    var itemCount = MutableLiveData<Int>()
    var discount = MutableLiveData<Int>()
    var gramCash = MutableLiveData<Int>()
    var totalAmount = MutableLiveData<Int>()
    var progress = MutableLiveData<Boolean>()
    var showGramCashCoinView = MutableLiveData<Boolean>()
    var showCartView = MutableLiveData<Boolean>()

    init {
        itemCount.value = 0
        discount.value = 0
        gramCash.value = 0
        totalAmount.value = 0
        progress.value = false
        showGramCashCoinView.value = true
        showCartView.value = false
    }

    fun onCheckedChange(button: CompoundButton, check: Boolean) {
        showGramCashCoinView.value = check
        if (check) {
            val total = totalAmount.value
            val gramCashCoin = gramCash.value
            val amount = total!! - gramCashCoin!!
            totalAmount.value = amount
        } else {
            val total = totalAmount.value
            val gramCashCoin = gramCash.value
            val amount = total!! + gramCashCoin!!
            totalAmount.value = amount
        }
    }

    fun onClickPlaceOrder() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.placeOrder()
                    progress.value = false
                    getNavigator()?.showToast(response.body()?.gp_api_message)
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        getNavigator()?.openCheckoutStatusActivity(Bundle().apply {
                            putString(Constants.ORDER_ID,
                                response.body()?.gp_api_response_data?.order_ref_id.toString())
                        })
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
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                    ) {
                        showCartView.value = true
                        itemCount.value = response.body()?.gp_api_response_data?.cart_items?.size
                        discount.value = response.body()?.gp_api_response_data?.total_discount
                        gramCash.value = response.body()?.gp_api_response_data?.gramcash_coins

                        val total = response.body()?.gp_api_response_data?.total
                        val gramCashCoin = response.body()?.gp_api_response_data?.gramcash_coins

                        val amount = total!! - gramCashCoin!!
                        totalAmount.value = amount

                        getNavigator()?.setCartAdapter(CartAdapter(response.body()?.gp_api_response_data?.cart_items!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity(ProductData(it.toInt()))
                            },
                            {
                                //on cartItem delete clicked
                                removeCartItem(it.toInt())
                            },
                            {
                                //on offer detail clicked
                                val promotionListItem = PromotionListItem()
                                promotionListItem.title = it.offer_name
                                promotionListItem.applicable_on_sku = it.valid_on_sku
                                promotionListItem.valid_till = it.valid_till
                               // promotionListItem.product_name = it.product_name
                                getNavigator()?.openActivity(
                                    OfferDetailActivity::class.java,
                                    Bundle().apply {
                                        putParcelable(Constants.OFFERSDATA, promotionListItem)

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
                    }
                    progress.value = false
                    getNavigator()?.showToast(response.body()?.gp_api_message)
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
