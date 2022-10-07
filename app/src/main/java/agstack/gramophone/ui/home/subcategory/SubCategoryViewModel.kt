package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.promotions.PromotionsRepository
import agstack.gramophone.ui.dialog.filter.FilterRequest
import agstack.gramophone.ui.dialog.filter.MainFilterData
import agstack.gramophone.ui.dialog.sortby.SortByData
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.model.*
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.amnix.xtension.extensions.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val promotionsRepository: PromotionsRepository,
) : BaseViewModel<SubCategoryNavigator>() {

    var productData = ObservableField<GpApiResponseDataProduct?>()
    var mSKUList = ArrayList<ProductSkuListItem?>()
    var showSubCategoryView = MutableLiveData<Boolean>()
    var toolbarTitle = MutableLiveData<String>()
    var toolbarImage = MutableLiveData<String>()
    var mainFilterList: ArrayList<MainFilterData>? = null
    var sortDataList: ArrayList<SortByData>? = null
    var subCategoryList: List<CategoryData>? = null
    var brandsList: List<Brands>? = null
    var cropsList: List<Crops>? = null
    var technicalDataList: List<TechnicalData>? = null
    var progress = MutableLiveData<Boolean>()
    var categoryId: String? = null
    var storeId: String? = null

    init {
        progress.value = false
        toolbarTitle.value = ""
        toolbarImage.value = ""
        showSubCategoryView.value = false
    }

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        initializeSortData()

        if (bundle?.containsKey(Constants.STORE_ID)!! && bundle.getString(Constants.STORE_ID) != null) {
            storeId = bundle.getString(Constants.STORE_ID)!!
            toolbarTitle.value = bundle.getString(Constants.STORE_NAME)!!
            toolbarImage.value = bundle.getString(Constants.STORE_IMAGE)!!
            getNavigator()?.showStoreCollapsing()
            getStoresFilterData()
        } else if (bundle.containsKey(Constants.CATEGORY_ID) && bundle.getString(Constants.CATEGORY_ID) != null) {
            categoryId = bundle.get(Constants.CATEGORY_ID) as String
            toolbarTitle.value = bundle.get(Constants.CATEGORY_NAME) as String
            toolbarImage.value = bundle.get(Constants.CATEGORY_IMAGE) as String
            getNavigator()?.showCategoryCollapsing()
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
                // do nothing
            }
        }
    }

    private fun getSubCategoryData() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response =
                        productRepository.getSubCategories(if (categoryId.isNull()) "" else categoryId!!)
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
                                subCategoryList) { id, name, image ->
                                /*getNavigator()?.openCheckoutStatusActivity(Bundle().apply {
                                putString(Constants.ORDER_ID,
                                    response.body()?.gp_api_response_data?.order_ref_id.toString())
                            })*/
                            })
                        }
                    } else {
                        brandsList = ArrayList()
                        cropsList = ArrayList()
                        technicalDataList = ArrayList()
                        getNavigator()?.disableSortAndFilter()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
                getAllProducts(Constants.RELAVENT_CODE,
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    "10",
                    "1")
            } catch (ex: Exception) {
                progress.value = false
                brandsList = ArrayList()
                cropsList = ArrayList()
                technicalDataList = ArrayList()
                getNavigator()?.disableSortAndFilter()
            }
        }
    }

    private fun getStoresFilterData() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response =
                        productRepository.getStoresFilterData(if (storeId.isNull()) "" else storeId!!)
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
                                subCategoryList) { id, name, image ->
                                /*getNavigator()?.openCheckoutStatusActivity(Bundle().apply {
                                putString(Constants.ORDER_ID,
                                    response.body()?.gp_api_response_data?.order_ref_id.toString())
                            })*/
                            })
                        }
                    } else {
                        brandsList = ArrayList()
                        cropsList = ArrayList()
                        technicalDataList = ArrayList()
                        getNavigator()?.disableSortAndFilter()
                    }
                } else {
                    getNavigator()?.showToast(getNavigator()?.getMessage(R.string.no_internet))
                }
                getAllProducts(Constants.RELAVENT_CODE,
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    "10",
                    "1")
            } catch (ex: Exception) {
                progress.value = false
                brandsList = ArrayList()
                cropsList = ArrayList()
                technicalDataList = ArrayList()
                getNavigator()?.disableSortAndFilter()
            }
        }
    }

    fun getAllProducts(
        sortBy: String,
        subCategoryIds: ArrayList<String>,
        brandIds: ArrayList<String>,
        cropIds: ArrayList<String>,
        technicalIds: ArrayList<String>,
        limit: String,
        page: String,
    ) {
        val filterRequest = FilterRequest(if (categoryId.isNullOrEmpty()) null else categoryId,
            sortBy,
            limit,
            page,
            if (storeId.isNullOrEmpty()) null else storeId,
            if (subCategoryIds.isNullOrEmpty()) null else subCategoryIds,
            if (brandIds.isNullOrEmpty()) null else brandIds,
            if (cropIds.isNullOrEmpty()) null else cropIds,
            if (technicalIds.isNullOrEmpty()) null else technicalIds)

        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val response = productRepository.getAllProducts(filterRequest)
                    progress.value = false

                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        getNavigator()?.setProductListAdapter(ProductListAdapter(response.body()?.gp_api_response_data?.data),
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
                // do nothing
            }
        }
    }

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
                            loadOffersData(productId)
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

    private fun loadOffersData(productId: Int) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true
                    val productData = ProductData()
                    productData.product_id = productId
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
                        gpiApiOfferResponse =
                            checkOfferResponse.body()?.gp_api_response_data!! as GpApiOfferResponse
                    } else {
                        isShowErrorMsg = true
                        errorMsg = checkOfferResponse.body()?.gp_api_message!!
                        gpiApiOfferResponse = null
                    }
                    getNavigator()?.updateAddToCartDialog(isShowErrorMsg, errorMsg)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onAddToCartClicked(productData: ProductData) {

        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    progress.value = true

                    val response =
                        productRepository.addToCart(productData)
                    progress.value = false
                    if (response.body()?.gp_api_status!!.equals(Constants.GP_API_STATUS)) {

                        getNavigator()?.showToast(response.body()?.gp_api_message)

                    } else {
                        getNavigator()?.showToast(response.body()?.gp_api_message)
                    }
                }
            } catch (e: Exception) {
                progress.value = false
            }
        }
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
}
