package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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

    fun getFeaturedProducts(hashMap: HashMap<Any, Any>) {
        viewModelScope.launch {
            try {
                //To get Product Data
                //homeRepository.getProducts(hashMap)
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
                //set received productList in Adapter

                getNavigator()?.setFeaturedProductsAdapter(ProductListAdapter(productList)) {
                    getNavigator()?.startProductDetailsActivity(it)
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
                val bannerResponse = SharedPreferencesHelper.instance?.getParcelable(
                    SharedPreferencesKeys.BANNER_DATA, BannerResponse::class.java
                )
                if (bannerResponse?.gpApiStatus == Constants.GP_API_STATUS) {
                    getNavigator()?.setViewPagerAdapter(bannerResponse.gpApiResponseData?.homeBanner1)
                }
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
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                    getNavigator()?.setCategoryAdapter(ShopByCategoryAdapter(response.body()?.gp_api_response_data?.product_app_categories_list)) {
                        getNavigator()?.openSubCategoryActivity(Bundle().apply {
                            putString(Constants.CATEGORY_ID,
                                it)
                        })
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

                    getNavigator()?.setCropAdapter(ShopByCropsAdapter(tempCropList)) {
                        getNavigator()?.openSubCategoryActivity(Bundle().apply {
                            putString(Constants.CROP_ID,
                                it)
                        })

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

                    getNavigator()?.setStoreAdapter(ShopByStoresAdapter(tempStoreList)) {
                        getNavigator()?.openSubCategoryActivity(Bundle().apply {
                            putString(Constants.STORE_ID,
                                it)
                        })

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

    fun getCompanies() {
        viewModelScope.launch {
            try {
                val response = productRepository.getCompanies()
                if (response.isSuccessful && response.body()?.gpApiStatus== Constants.GP_API_STATUS
                    && response.body()?.gpApiResponseData?.companiesList?.size!! > 0
                ) {
                    companyResponse = response.body()
                    val companyList = ArrayList<CompanyData>()
                    companyList.addAll(response.body()?.gpApiResponseData?.companiesList!!)

                    val tempCompanyList: List<CompanyData> = if (companyList.size >= 6)
                        companyList.subList(0, 6)
                    else companyList
                    getNavigator()?.setCompanyAdapter(ShopByCompanyAdapter(tempCompanyList)) {
                        /*getNavigator()?.openSubCategoryActivity(Bundle().apply {
                            putString(Constants.COMPANIES_ID,
                                it)
                        })*/
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
}