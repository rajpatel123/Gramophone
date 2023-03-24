package agstack.gramophone.ui.home.shop

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.adapter.ShopByCompanyAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.adapter.ShopByStoresAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ShopByViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ShopByNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()
    var shopByType: String = ""

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.SHOP_BY_TYPE)!! && bundle.getString(Constants.SHOP_BY_TYPE) != null) {
            shopByType = bundle.getString(Constants.SHOP_BY_TYPE)!!
            when (shopByType) {
                Constants.SHOP_BY_CROP -> {
//                    showWeatherView.value = true
//                    val cropResponse: CropResponse =
//                        bundle.getParcelable<ProductData>(Constants.SHOP_BY_CROP) as CropResponse
//                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_crops)!!)
//                    getNavigator()?.setShopByCropAdapter(ShopByCropsAdapter(cropResponse.gpApiResponseData?.cropsList) {
//                        getNavigator()?.openCropStageActivity(it)
//                    }) {
//                        getNavigator()?.openCropStageActivity(it)
//                    }
                }
                Constants.SHOP_BY_STORE -> {
                    showWeatherView.value = true
                    val storeResponse: StoreResponse =
                        bundle.getParcelable<StoreResponse>(Constants.SHOP_BY_STORE) as StoreResponse
                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_store)!!)
                    getNavigator()?.setShopByStoresAdapter(ShopByStoresAdapter(storeResponse.gpApiResponseData?.storeList) { id, name, image ->
                        getNavigator()?.openFeaturedActivityForShopByStore(id, name, image)
                    })
                }
                Constants.SHOP_BY_COMPANY -> {
                    showWeatherView.value = true
                    val companyResponse: CompanyResponse =
                        bundle.getParcelable<CompanyResponse>(Constants.SHOP_BY_COMPANY) as CompanyResponse
                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_company)!!)
                    getNavigator()?.setShopByCompanyAdapter(ShopByCompanyAdapter(companyResponse.gpApiResponseData?.companiesList) { id, name, image ->
                        getNavigator()?.openFeaturedActivityForShopByCompany(id, name, image)
                    })
                }
            }
        }
    }

    fun getStores() {
        viewModelScope.launch {
            try {
                when (shopByType) {
                    Constants.SHOP_BY_STORE -> {
                        progress.value = true
                        val response = productRepository.getStores()
                        if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                            && response.body()?.gpApiResponseData?.storeList?.size!! > 0
                        ) {
                            progress.value = false
                            getNavigator()?.setShopByStoresAdapter(ShopByStoresAdapter(response.body()?.gpApiResponseData?.storeList) { id, name, image ->
                                getNavigator()?.openFeaturedActivityForShopByStore(id, name, image)
                            })
                        }
                    }
                    Constants.SHOP_BY_COMPANY -> {
                        progress.value = true
                        val response = productRepository.getCompanies()
                        if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS
                            && response.body()?.gpApiResponseData?.companiesList?.size!! > 0
                        ) {
                            progress.value = false
                            getNavigator()?.setShopByCompanyAdapter(ShopByCompanyAdapter(response.body()?.gpApiResponseData?.companiesList) { id, name, image ->
                                getNavigator()?.openFeaturedActivityForShopByCompany(id, name, image)
                            })
                        }
                    }
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
