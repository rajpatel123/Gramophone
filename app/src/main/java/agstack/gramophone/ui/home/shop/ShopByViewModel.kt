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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ShopByViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<ShopByNavigator>() {

    var progress = MutableLiveData<Boolean>()
    var isRainView = MutableLiveData<Boolean>()
    var showWeatherView = MutableLiveData<Boolean>()

    init {
        progress.value = false
        isRainView.value = false
        showWeatherView.value = false
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.containsKey(Constants.SHOP_BY_TYPE)!! && bundle.getString(Constants.SHOP_BY_TYPE) != null) {
            when (bundle.getString(Constants.SHOP_BY_TYPE)) {
                Constants.SHOP_BY_CROP -> {
                    showWeatherView.value = true
                    val cropResponse: CropResponse = bundle.getParcelable<ProductData>(Constants.SHOP_BY_CROP) as CropResponse
                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_crops)!!)
                    getNavigator()?.setShopByCropAdapter(ShopByCropsAdapter(cropResponse.gpApiResponseData?.cropsList) {
                        getNavigator()?.openCropStageActivity(it)
                    }) {
                        getNavigator()?.openCropStageActivity(it)
                    }
                }
                Constants.SHOP_BY_STORE -> {
                    showWeatherView.value = true
                    val storeResponse: StoreResponse = bundle.getParcelable<ProductData>(Constants.SHOP_BY_STORE) as StoreResponse
                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_store)!!)
                    getNavigator()?.setShopByStoresAdapter(ShopByStoresAdapter(storeResponse.gpApiResponseData?.storeList) {
                        getNavigator()?.openShopByDetailActivity(it)
                    }) {
                        getNavigator()?.openShopByDetailActivity(it)
                    }
                }
                Constants.SHOP_BY_COMPANY -> {
                    showWeatherView.value = true
                    val companyResponse: CompanyResponse = bundle.getParcelable<ProductData>(Constants.SHOP_BY_COMPANY) as CompanyResponse
                    getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.shop_by_company)!!)
                    getNavigator()?.setShopByCompanyAdapter(ShopByCompanyAdapter(companyResponse.gpApiResponseData?.companiesList) {
                        getNavigator()?.openShopByDetailActivity(it)
                    }) {
                        getNavigator()?.openShopByDetailActivity(it)
                    }
                }
            }
        }
    }

}
