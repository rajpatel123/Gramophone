package agstack.gramophone.ui.home.subcategory

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.dialog.filter.FilterRequest
import agstack.gramophone.ui.dialog.filter.MainFilterData
import agstack.gramophone.ui.dialog.sortby.SortByData
import agstack.gramophone.ui.home.adapter.ShopByCategoryAdapter
import agstack.gramophone.ui.home.subcategory.model.Brands
import agstack.gramophone.ui.home.subcategory.model.Crops
import agstack.gramophone.ui.home.subcategory.model.Offer
import agstack.gramophone.ui.home.subcategory.model.TechnicalData
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.amnix.xtension.extensions.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<SubCategoryNavigator>() {

    var productData = ObservableField<GpApiResponseDataProduct?>()
    var mSKUList = ArrayList<ProductSkuListItem?>()
    var showSubCategoryView = MutableLiveData<Boolean>()
    var showSortFilterView = MutableLiveData<Boolean>()
    var showSortFilterInToolbar = MutableLiveData<Boolean>()
    var toolbarTitle = MutableLiveData<String>()
    var toolbarImage = MutableLiveData<String>()
    var mainFilterList: ArrayList<MainFilterData>? = null
    var sortDataList: ArrayList<SortByData> = ArrayList<SortByData>()
    var subCategoryList: List<CategoryData>? = null
    var brandsList: List<Brands>? = null
    var cropsList: List<Crops>? = null
    var technicalDataList: List<TechnicalData>? = null
    var progress = MutableLiveData<Boolean>()
    val subCategoryIds = ArrayList<String>()
    val companyIds = ArrayList<String>()
    var categoryId: String? = null
    var storeId: String? = null
    private var checkOfferApplicableJob: Job? = null

    init {
        progress.value = false
        toolbarTitle.value = ""
        toolbarImage.value = ""
        showSortFilterView.value = false
        showSortFilterInToolbar.value = false
        showSubCategoryView.value = false
    }

    fun getBundleData() {
        subCategoryIds.clear()
        companyIds.clear()
        val bundle = getNavigator()?.getBundle()
        initializeSortData()
        if (bundle.isNotNull()) {
            if (bundle?.containsKey(Constants.HOME_FEATURED_PRODUCTS)!! && bundle.getString(
                    Constants.HOME_FEATURED_PRODUCTS) != null
            ) {
                showSortFilterView.value = false
                toolbarTitle.value = getNavigator()?.getMessage(R.string.featured_products)
                getFeaturedProducts()
            } else if (bundle.containsKey(Constants.STORE_ID) && bundle.getString(Constants.STORE_ID) != null) {
                showSortFilterView.value = true
                storeId = bundle.getString(Constants.STORE_ID)!!
                toolbarTitle.value = bundle.getString(Constants.STORE_NAME)!!
                toolbarImage.value = bundle.getString(Constants.STORE_IMAGE)!!
                getStoresFilterData()
                getShopByStoreBanner()
            } else if (bundle.containsKey(Constants.CATEGORY_ID) && bundle.getString(Constants.CATEGORY_ID) != null) {
                categoryId = bundle.get(Constants.CATEGORY_ID) as String
                toolbarTitle.value = bundle.get(Constants.CATEGORY_NAME) as String
                toolbarImage.value = bundle.get(Constants.CATEGORY_IMAGE) as String
                getSubCategoryData()
            } else if (bundle.containsKey(Constants.SUB_CATEGORY_ID) && bundle.getString(Constants.SUB_CATEGORY_ID) != null) {
                showSortFilterView.value = true
                categoryId = bundle.getString(Constants.SHOP_BY_SUB_CATEGORY)
                subCategoryIds.add(bundle.getString(Constants.SUB_CATEGORY_ID, ""))
                toolbarTitle.value = bundle.getString(Constants.SUB_CATEGORY_NAME)
                toolbarImage.value = bundle.getString(Constants.SUB_CATEGORY_IMAGE)
                getSubCategoryData()
            } else if (bundle.containsKey(Constants.COMPANY_ID) && bundle.getString(Constants.COMPANY_ID) != null) {
                showSortFilterView.value = false
                showSortFilterInToolbar.value = true
                companyIds.add(bundle.getString(Constants.COMPANY_ID)!!)
                toolbarTitle.value = bundle.getString(Constants.COMPANY_NAME)!!
                toolbarImage.value = bundle.getString(Constants.COMPANY_IMAGE)!!
                getAllProducts(Constants.RELAVENT_CODE,
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    Constants.API_DATA_LIMITS_IN_ONE_TIME,
                    "1")
            } else if (bundle.containsKey(Constants.PAGE_URL) || bundle.containsKey(Constants.PAGE_SOURCE)) {
                var webUrl = ""
                if (bundle.containsKey(Constants.PAGE_URL) && bundle.getString(Constants.PAGE_URL) != null) {
                    webUrl = bundle.get(Constants.PAGE_URL).toString()
                    if (webUrl.isNotNullOrEmpty() && !webUrl.contains("single-article") && !webUrl.contains(
                            "?")
                    ) {
                        webUrl += "?" + Constants.LANG + "=" + getNavigator()?.getLanguage() + "&" + Constants.GP_TOKEN + "=" + SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.session_token)!!
                    }
                }

                Log.d("URL","".plus(webUrl.isNotNullOrEmpty() && !webUrl.contains("single-article") && !webUrl.contains(
                    "?")).plus(webUrl))
                if (webUrl.isNotNullOrEmpty())
                    getNavigator()?.loadUrl(webUrl)


                if (webUrl.isNotNullOrEmpty() && bundle.containsKey(Constants.PAGE_SOURCE)) {
                    viewModelScope.launch {
                        delay(1000)
                        getNavigator()?.reload()

                    }
                }
            }
        }
    }

    private fun initializeSortData() {
        if (sortDataList.isNull()) {
            sortDataList = ArrayList<SortByData>()
        } else {
            sortDataList.clear()
        }
        sortDataList.add(SortByData(true, Constants.RELEVANCE, Constants.RELAVENT_CODE))
        sortDataList.add(SortByData(false,
            Constants.AVG_CUSTOMER_RATING,
            Constants.AVG_CUSTOMER_RATING_CODE))
        sortDataList.add(SortByData(false,
            Constants.PRICE_LOW_TO_HIGH,
            Constants.PRICE_LOW_TO_HIGH_CODE))
        sortDataList.add(SortByData(false,
            Constants.PRICE_HIGH_TO_LOW,
            Constants.PRICE_HIGH_TO_LOW_CODE))
    }

    private fun initMainFilterData() {
        mainFilterList = ArrayList()
        if (!subCategoryList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(true, false,
            Constants.SUB_CATEGORY, 0, 0))
        if (!brandsList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false, false,
            Constants.BRAND,
            0, 0))
        if (!cropsList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false, false,
            Constants.CROP,
            0, 0))
        if (!technicalDataList.isNullOrEmpty()) mainFilterList?.add(MainFilterData(false, false,
            Constants.TECHNICAL, 0, 0))
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

    fun getShopByStoreBanner() {
        viewModelScope.launch {
            try {
                val bannerResponse = SharedPreferencesHelper.instance?.getParcelable(
                    SharedPreferencesKeys.BANNER_DATA, BannerResponse::class.java
                )
                if (bannerResponse?.gpApiStatus == Constants.GP_API_STATUS) {
                    getNavigator()?.setViewPagerAdapter(bannerResponse.gpApiResponseData?.shopByStore)
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
                        // subCategoryIds.isNullOrEmpty() this check is placed here
                        // because It has no need of sub category detail page and only used in category detail page
                        if (subCategoryIds.isNullOrEmpty()) {
                            subCategoryList =
                                response.body()?.gp_api_response_data?.product_app_sub_categories_list
                            if (subCategoryList != null && subCategoryList?.size!! > 0
                            ) {
                                showSubCategoryView.value = true
                                getNavigator()?.setSubCategoryAdapter(ShopByCategoryAdapter(
                                    subCategoryList) { subCategoryId, subCategoryName, subCategoryImage ->
                                    getNavigator()?.getSubCategoryDetail(categoryId!!,
                                        subCategoryId,
                                        subCategoryName,
                                        subCategoryImage)
                                })
                            }
                        }
                        initMainFilterData()
                        getNavigator()?.enableSortAndFilter()
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
                    subCategoryIds,
                    ArrayList(),
                    ArrayList(),
                    ArrayList(),
                    Constants.API_DATA_LIMITS_IN_ONE_TIME,
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
                    Constants.API_DATA_LIMITS_IN_ONE_TIME,
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
            if (companyIds.isNullOrEmpty()) null else companyIds,
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

    fun fetchProductDetail(productId: Int) {
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

                    val offersResponse = productRepository.getOffersOnProduct(productData)
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

    fun checkOfferApplicability(
        verifyPromotionsModel: VerifyPromotionRequestModel,
    ) {
        checkOfferApplicableJob.cancelIfActive()
        checkOfferApplicableJob = checkNetworkThenRun {
            try {
                val response =
                    productRepository.checkPromotionOnProduct(verifyPromotionsModel)

                if (response.body()?.gpApiStatus.equals(Constants.GP_API_STATUS) &&
                    response.body()?.gpApiResponseData?.promotionApplicable == true
                ) {
                    getNavigator()?.updateOfferApplicabilityOnDialog(true,
                        verifyPromotionsModel.promotion_id,
                        if (response.body()?.gpApiMessage.isNull()) "" else response.body()?.gpApiMessage!!)
                } else {
                    getNavigator()?.updateOfferApplicabilityOnDialog(false,
                        verifyPromotionsModel.promotion_id,
                        if (response.body()?.gpApiMessage.isNull()) "" else response.body()?.gpApiMessage!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getNavigator()?.updateOfferApplicabilityOnDialog(false, null,
                    if (e.message.isNull()) "" else e.message!!)
            }
        }
    }

    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                    if (response.body()?.gp_api_status!! == Constants.GP_API_STATUS) {

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
                        productRepository.getFeaturedProducts(PageLimitRequest(Constants.API_DATA_LIMITS_IN_ONE_TIME,
                            "1"))
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
