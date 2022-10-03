package agstack.gramophone.ui.home.featured

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.ui.home.subcategory.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.model.*
import agstack.gramophone.ui.home.view.fragments.market.model.GpApiResponseDataProduct
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionsRepository: PromotionsRepository,
) : BaseViewModel<FeaturedNavigator>() {

    var progress = MutableLiveData<Boolean>()

    init {
        progress.value = false
    }

    fun getFeaturedProducts() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response =
                        productRepository.getFeaturedProducts(PageLimitRequest("20", "1"))
                    progress.value = false

                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        getNavigator()?.setProductListAdapter(ProductListAdapter(
                            response.body()?.gp_api_response_data?.data),
                            {
                                fetchProductDetail(it)
                            }, {
                                getNavigator()?.openProductDetailsActivity(ProductData(it))
                            })
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
            } catch (ex: Exception) {
                progress.value = false
            }
        }
    }
    var productData = ObservableField<GpApiResponseDataProduct?>()
    var mSKUList = ArrayList<ProductSkuListItem?>()
    private fun fetchProductDetail(productId: Int) {
        val product = ProductData(productId)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val productDetailResponse = productRepository
                        .getProductData(product)
                    //stop loader
                    progress.value = false
                    if (productDetailResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                        productData.set(productDetailResponse.body()?.gpApiResponseData!!)
                        productData.let {
                            //set skuList
                            mSKUList =
                                productData.get()?.productSkuList as ArrayList<ProductSkuListItem?>
                            loadOffersData(productId, productData.get()?.productBaseName!!)
                        }
                    } else {
                        getNavigator()?.showToast(productDetailResponse.body()?.gpApiMessage)
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    private fun loadOffersData(productId: Int, productBaseName: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val productData = ProductData()
                    productData.product_id = productId
                    productData.product_base_name = productBaseName
                    productData.quantity = 1

                    var offerList = ArrayList<Offer>()

                    val offersResponse =
                        productRepository.getApplicableOffersOnProduct(ApplicableOfferRequest("BASF | XELORA"))
                    progress.value = false
                    if (offersResponse.isSuccessful && offersResponse.body()?.gp_api_response_data.isNotNull()
                        && offersResponse.body()?.gp_api_response_data?.offers.isNotNullOrEmpty()
                    ) {
                        offerList =
                            offersResponse.body()?.gp_api_response_data?.offers as ArrayList<Offer>
                    }

                    getNavigator()?.openAddToCartDialog(mSKUList,
                        offerList,
                        productData)
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    fun applyOfferOnProduct(offerForProduct: OfferForProduct) {
        val products = ArrayList<OfferForProduct>()
        products.add(offerForProduct)
        val checkOfferRequest = CheckOfferRequest(
            "krishi app",
            "customer",
            SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!,
            "app",
            products,
            "app"
        )

        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {

                    val checkOfferResponse =
                        promotionsRepository.checkOfferOnProduct(checkOfferRequest)
                    val errorMsg: String
                    val isShowErrorMsg: Boolean
                    val gpiApiOfferResponse: GpApiOfferResponse?
                    if (checkOfferResponse.body()?.gp_api_status.equals(Constants.GP_API_STATUS)) {
                        isShowErrorMsg = false
                        errorMsg = ""
                        gpiApiOfferResponse = checkOfferResponse.body()?.gp_api_response_data!! as GpApiOfferResponse
                    } else {
                        isShowErrorMsg = true
                        errorMsg = checkOfferResponse.body()?.gp_api_message!!
                        gpiApiOfferResponse = null
                    }
                    getNavigator()?.updateAddToCartDialog(isShowErrorMsg, errorMsg, gpiApiOfferResponse!!)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onAddToCartClicked(productToBeAdded: ProductData) {

        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val addTocartResponse =
                        productRepository.addToCart(productToBeAdded)
                    progress.value = false
                    if (addTocartResponse.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)

                    } else {
                        getNavigator()?.showToast(addTocartResponse.body()?.gp_api_message)
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }
}
