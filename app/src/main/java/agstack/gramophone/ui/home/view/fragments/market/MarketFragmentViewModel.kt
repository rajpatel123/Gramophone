package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MarketFragmentViewModel
@Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<MarketFragmentNavigator>() {

    var productList = ArrayList<ProductData>()
    var cropResponse: CropResponse? = null
    var storeResponse: StoreResponse? = null
    var companyResponse: CompanyResponse? = null
    var allBannerResponse: BannerResponse? = null
    var categoryResponse: CategoryResponse? = null
    var cartList: List<CartItem>? = arrayListOf()

    fun getHomeData() {
        productList.clear()
        cropResponse = null
        storeResponse = null
        companyResponse = null
        allBannerResponse = null
        categoryResponse = null
        cartList = null

        viewModelScope.launch {
            try {
                val response = productRepository.getHomeData()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                    getNavigator()?.setHomeAdapter(HomeAdapter(response.body()?.gp_api_response_data?.home_screen_sequence!!,
                        allBannerResponse, categoryResponse, productList,
                        cropResponse,
                        storeResponse, companyResponse, cartList)) {
                    }
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getBanners() {
        viewModelScope.launch {
            try {
                var bannerResponse = SharedPreferencesHelper.instance?.getParcelable(
                    SharedPreferencesKeys.BANNER_DATA, BannerResponse::class.java
                )
                allBannerResponse = bannerResponse
                if (bannerResponse?.gpApiStatus != Constants.GP_API_STATUS) {
                    val response = productRepository.getBanners()
                    if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS) {
                        bannerResponse = response.body()
                        allBannerResponse = bannerResponse
                        SharedPreferencesHelper.instance?.putParcelable(
                            SharedPreferencesKeys.BANNER_DATA,
                            bannerResponse!!
                        )
                    }
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getFeaturedProducts() {
        viewModelScope.launch {
            try {
                //To get Product Data
                //homeRepository.getProducts(hashMap)
                if (productList.isNotEmpty()) productList.clear()
                val productImagesList = ArrayList<String>()
                productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
                productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
                productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
                productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
                productList.add(
                    ProductData(700322)
                )
                productList.add(
                    ProductData(700322)
                )
                productList.add(
                    ProductData(700322)
                )
                productList.add(
                    ProductData(700322)
                )
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }

    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = productRepository.getCategories()
                categoryResponse = response.body()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS &&
                    response.body()?.gp_api_response_data?.product_app_categories_list != null
                ) {

                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getCrops() {
        viewModelScope.launch {
            try {
                val response = productRepository.getCrops()
                if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                    && response.body()?.gpApiResponseData?.cropsList?.size!! > 0
                ) {
                    cropResponse = response.body()
                    val cropList = ArrayList<CropData>()
                    cropList.addAll(cropResponse?.gpApiResponseData?.cropsList!!)

                    val tempCropList: List<CropData> = if (cropList.size >= 9)
                        cropList.subList(0, 9)
                    else cropList
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getStores() {
        viewModelScope.launch {
            try {
                val response = productRepository.getStores()
                if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                    && response.body()?.gpApiResponseData?.storeList?.size!! > 0
                ) {
                    storeResponse = response.body()
                    val storeList = ArrayList<StoreData>()
                    storeList.addAll(response.body()?.gpApiResponseData?.storeList!!)

                    val tempStoreList: List<StoreData> = if (storeList.size >= 4)
                        storeList.subList(0, 4)
                    else storeList
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getCompanies() {
        viewModelScope.launch {
            try {
                val response = productRepository.getCompanies()
                if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                    && response.body()?.gpApiResponseData?.companiesList?.size!! > 0
                ) {
                    companyResponse = response.body()
                    val companyList = ArrayList<CompanyData>()
                    companyList.addAll(response.body()?.gpApiResponseData?.companiesList!!)

                    val tempCompanyList: List<CompanyData> = if (companyList.size >= 6)
                        companyList.subList(0, 6)
                    else companyList
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    fun getCartProducts() {
        viewModelScope.launch {
            try {
                val response = productRepository.getCartData()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                ) {
                    cartList = response.body()?.gp_api_response_data?.cart_items
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun notifyAdapter() {
        getNavigator()?.notifyHomeAdapter(
            allBannerResponse, categoryResponse, productList,
            cropResponse,
            storeResponse, companyResponse, cartList)
    }
}