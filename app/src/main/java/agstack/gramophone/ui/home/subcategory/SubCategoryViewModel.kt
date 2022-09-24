package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.dialog.filter.MainFilterData
import agstack.gramophone.ui.dialog.sortby.SortByData
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.model.Brands
import agstack.gramophone.ui.home.subcategory.model.Crops
import agstack.gramophone.ui.home.subcategory.model.TechnicalData
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<SubCategoryNavigator>() {

    var productData = ObservableField<GpApiResponseDataProduct?>()
    var mSKUList = ArrayList<ProductSkuListItem?>()
    var mSkuOfferList = ArrayList<PromotionListItem?>()
    var showSubCategoryView = MutableLiveData<Boolean>()
    var mainFilterList: ArrayList<MainFilterData>? = null
    var sortDataList: ArrayList<SortByData>? = null
    var subCategoryList: List<CategoryData>? = null
    var brandsList: List<Brands>? = null
    var cropsList: List<Crops>? = null
    var technicalDataList: List<TechnicalData>? = null
    var progress = MutableLiveData<Boolean>()
    var categoryId: String = ""

    init {
        progress.value = false
        showSubCategoryView.value = false
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        initializeSortData()
        if (bundle?.containsKey(Constants.CATEGORY_ID)!! && bundle.getString(Constants.CATEGORY_ID) != null) {
            categoryId = bundle.get(Constants.CATEGORY_ID) as String
            getSubCategoryData()
        }
    }

    private fun initializeSortData() {
        sortDataList = ArrayList<SortByData>()
        sortDataList?.add(SortByData(true, Constants.RELAVENT, Constants.RELAVENT_CODE))
        sortDataList?.add(SortByData(false,
            Constants.AVG_CUSTOMER_RATING,
            Constants.AVG_CUSTOMER_RATING_CODE))
        sortDataList?.add(SortByData(false,
            Constants.PRICE_LOW_TO_HIGH,
            Constants.PRICE_LOW_TO_HIGH_CODE))
        sortDataList?.add(SortByData(false,
            Constants.PRICE_HIGH_TO_LOW,
            Constants.PRICE_HIGH_TO_LOW_CODE))
    }

    private fun initMainFilterData() {
        mainFilterList = ArrayList()
        if (!subCategoryList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(true,
            Constants.SUB_CATEGORY, 0))
        if (!brandsList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false,
            Constants.BRAND,
            0))
        if (!cropsList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false,
            Constants.CROP,
            0))
        if (!technicalDataList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false,
            Constants.TECHNICAL, 0))
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

    private fun getSubCategoryData() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.getSubCategories(categoryId)
                    progress.value = false

                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        brandsList = response.body()?.gp_api_response_data?.brands_list
                        cropsList = response.body()?.gp_api_response_data?.crops_list
                        technicalDataList = response.body()?.gp_api_response_data?.technical_data
                        subCategoryList =
                            response.body()?.gp_api_response_data?.product_app_sub_categories_list
                        initMainFilterData()
                        getNavigator()?.enableSortAndFilter()

                        if (subCategoryList != null && subCategoryList?.size!! > 0
                        ) {
                            showSubCategoryView.value = true
                            getNavigator()?.setSubCategoryAdapter(ShopByCategoryAdapter(
                                subCategoryList) {
                                /*getNavigator()?.openCheckoutStatusActivity(Bundle().apply {
                                putString(Constants.ORDER_ID,
                                    response.body()?.gp_api_response_data?.order_ref_id.toString())
                            })*/
                            })
                        }
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
                viewModelScope.launch {
                    progress.value = true
                    delay(1500)
                    setAdapter()
                    progress.value = false
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

    fun setAdapter() {
        getNavigator()?.setProductListAdapter(ProductListAdapter()) {
            fetchProductDetail()
        }
    }

    private fun fetchProductDetail() {
        val product = ProductData(700322)
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val productAPIResponse = productRepository
                        .getProductData(product)
                    //stop loader
                    progress.value = false
                    if (productAPIResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                        productData.set(productAPIResponse.body()?.gpApiResponseData!!)
                        productData.let {
                            val productResponseData = productData.get()

                            //set skuList
                            mSKUList =
                                productData.get()?.productSkuList as ArrayList<ProductSkuListItem?>


                        }
                        loadOffersData(product)

                    } else {
                        getNavigator()?.showToast(productAPIResponse.body()?.gpApiMessage)
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }

    var selectedOfferItem = PromotionListItem()
    private fun loadOffersData(productDetailstoBeFetched: ProductData, quantity: Int? = 0) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    if (quantity!! > 0) {
                        productDetailstoBeFetched.quantity = quantity
                    }
                    val offersOnProductResponse =
                        productRepository.getOffersOnProductData(ProductData(700322))
                    progress.value = false
                    if (offersOnProductResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                        //setOffer List
                        offersOnProductResponse.body()?.gpApiResponseData?.offersProductList.let {
                            val prodOfferList =
                                ArrayList(offersOnProductResponse.body()?.gpApiResponseData?.offersProductList!!)
                            prodOfferList.let {
                                mSkuOfferList = ArrayList(prodOfferList)
                                getNavigator()?.openAddToCartDialog(mSKUList, mSkuOfferList)
                                selectedOfferItem.let {
                                    if (mSkuOfferList.contains(selectedOfferItem)) {
                                        for (item in mSkuOfferList) {
                                            if (item!!.equals(selectedOfferItem)) {
                                                item.selected = true
                                            }
                                            /*getNavigator()?.refreshOfferAdapter()*/
                                        }
                                    } else {
                                        //do nothing as a new list is loaded with selected = false
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
    }
}
