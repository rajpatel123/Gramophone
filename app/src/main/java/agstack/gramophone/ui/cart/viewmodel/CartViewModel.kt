package agstack.gramophone.ui.cart.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.RemoveCartItemResponse
import agstack.gramophone.ui.home.product.activity.ProductSKUOfferAdapter
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.os.Bundle
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

    val removeCartItemResponse: MutableLiveData<ApiResponse<RemoveCartItemResponse>> =
        MutableLiveData()

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
                        getNavigator()?.setCartAdapter(
                            CartAdapter(response.body()?.gp_api_response_data?.cart_items!!),
                            {
                                //on cartItem clicked for details page
                                getNavigator()?.openProductDetailsActivity()
                            },
                            {
                                //on cartItem delete clicked
                                getNavigator()?.deleteCartItem(it)
                            },
                            {
                                //on offer clicked
                                getNavigator()?.openAppliedOfferDetailActivity()
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

    fun removeCartItem() {
        /*viewModelScope.launch {

            removeCartItemResponse.postValue(ApiResponse.Loading())
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = productRepository.removeCartItem()
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                        removeCartItemResponse.postValue(ApiResponse.Success(response.body()!!))
                    } else {
                        removeCartItemResponse.postValue(ApiResponse.Error(response.message()))
                    }
                } else
                    removeCartItemResponse.postValue(ApiResponse.Error(getNavigator()?.getMessage(R.string.no_internet)!!))
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> removeCartItemResponse.postValue(
                        ApiResponse.Error(
                            getNavigator()?.getMessage(
                                R.string.network_failure
                            )!!
                        )
                    )
                    else -> removeCartItemResponse.postValue(
                        ApiResponse.Error(
                            getNavigator()?.getMessage(
                                R.string.some_thing_went_wrong
                            )!!
                        )
                    )
                }
            }
        }*/
    }
}
