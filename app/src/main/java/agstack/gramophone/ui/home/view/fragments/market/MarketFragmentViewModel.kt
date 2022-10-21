package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.articles.ArticlesRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.farm.model.FarmRequest
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MarketFragmentViewModel
@Inject constructor(
    private val productRepository: ProductRepository,
    private val articlesRepository: ArticlesRepository,
) : BaseViewModel<MarketFragmentNavigator>() {

    var allProductsResponse: AllProductsResponse? = null
    var cropResponse: CropResponse? = null
    var storeResponse: StoreResponse? = null
    var companyResponse: CompanyResponse? = null
    var allBannerResponse: BannerResponse? = null
    var categoryResponse: CategoryResponse? = null
    var cartList: List<CartItem>? = arrayListOf()
    var farmResponse: FarmResponse? = null
    var articlesData: HashMap<String, ArrayList<FormattedArticlesData>> = HashMap()

    fun showAppTourDialogIfApplicable() {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.APP_TOUR_ENABLED) == true
            && SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.APP_TOUR_SKIP_COUNT)!! < 2
        ) {
            getNavigator()?.showAppTourDialog()
        }
    }

    fun getHomeData() {
        allProductsResponse = null
        cropResponse = null
        storeResponse = null
        companyResponse = null
        allBannerResponse = null
        categoryResponse = null
        cartList = null
        farmResponse = null

        viewModelScope.launch {
            try {
                val response = productRepository.getHomeData()
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS) {
                    val list = response.body()?.gp_api_response_data?.home_screen_sequence!!
                    getNavigator()?.setHomeAdapter(HomeAdapter(list,
                        allBannerResponse,
                        categoryResponse,
                        allProductsResponse,
                        cropResponse,
                        storeResponse,
                        companyResponse,
                        cartList,
                        farmResponse,
                        articlesData)) {
                        getNavigator()?.launchCommunityFragment()
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
                val response = productRepository.getFeaturedProducts(PageLimitRequest("10", "1"))
                allProductsResponse = response.body()
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

    fun getFarms() {
        viewModelScope.launch {
            try {
                val response = productRepository.getFarmsData("active", FarmRequest("3", "1"))
                if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                    && response.body()?.gp_api_response_data != null
                ) {
                    farmResponse = response.body()
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

    fun getFeaturedArticles() {
        viewModelScope.launch {
            try {
                val response = articlesRepository.getFeaturedArticles()
                if (response.isSuccessful) {
                    val featuredArticleResponse = response.body()
                    val featuredArticlesList = ArrayList<FormattedArticlesData>()

                    if (featuredArticleResponse != null) {
                        for (item in featuredArticleResponse) {
                            val id = item.id
                            val title =
                                if (item.title != null && item.title.rendered.isNotNullOrEmpty()) item.title.rendered else ""
                            val minToRead =
                                if (item.min_to_read.isNotNullOrEmpty()) item.min_to_read else ""
                            val postViewCount =
                                if (item.post_views.isNotNullOrEmpty()) item.post_views else ""
                            var tag = ""
                            if (item.acf!= null && item.acf !is Boolean) {
                                tag = try {
                                    val categoryName: String = (item.acf as Map<*, *>)["category_name"] as String
                                    if (categoryName.isNotNullOrEmpty()) categoryName else ""
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    ""
                                }
                            }
                            var imageUrl = ""
                            try {
                                if (item._embedded != null && item._embedded.featuredmedia != null && item._embedded.featuredmedia.size > 0) {
                                    if (item._embedded.featuredmedia[0].media_details != null &&
                                        item._embedded.featuredmedia[0].media_details!!.sizes != null && item._embedded.featuredmedia[0].media_details!!.sizes!!.thumbnail != null
                                        && item._embedded.featuredmedia[0].media_details!!.sizes!!.thumbnail!!.source_url.isNotNullOrEmpty()
                                    ) {
                                        imageUrl =
                                            item._embedded.featuredmedia[0].media_details!!.sizes!!.thumbnail!!.source_url!!
                                    } else {
                                        imageUrl = item._embedded.featuredmedia[0].source_url!!
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                imageUrl = ""
                            }
                            val formattedArticlesData = FormattedArticlesData()
                            formattedArticlesData.id = id
                            formattedArticlesData.title = title
                            formattedArticlesData.min_to_read = minToRead
                            formattedArticlesData.post_views = postViewCount
                            formattedArticlesData.tag = tag
                            formattedArticlesData.image_url = imageUrl
                            formattedArticlesData.articleTye = Constants.FEATURED_ARTICLES
                            featuredArticlesList.add(formattedArticlesData)
                        }
                    }
                    articlesData[Constants.FEATURED_ARTICLES] = featuredArticlesList
                }
                getTrendingArticles()
            } catch (ex: Exception) {
                getTrendingArticles()
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun getTrendingArticles() {
        viewModelScope.launch {
            try {
                val response = articlesRepository.getTrendingArticles()
                if (response.isSuccessful) {
                    val featuredArticleResponse = response.body()
                    val trendingArticlesList = ArrayList<FormattedArticlesData>()

                    if (featuredArticleResponse != null) {
                        for (item in featuredArticleResponse) {
                            val id = item.id
                            val title =
                                if (item.title != null && item.title.rendered.isNotNullOrEmpty()) item.title.rendered else ""
                            val minToRead =
                                if (item.min_to_read.isNotNullOrEmpty()) item.min_to_read else ""
                            val postViewCount =
                                if (item.post_views.isNotNullOrEmpty()) item.post_views else ""
                            var tag = ""
                            if (item.acf!= null && item.acf !is Boolean) {
                                tag = try {
                                    val categoryName: String = (item.acf as Map<*, *>)["category_name"] as String
                                    if (categoryName.isNotNullOrEmpty()) categoryName else ""
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    ""
                                }
                            }
                            val imageUrl =
                                if (item.featured_image.isNotNullOrEmpty()) item.featured_image else ""

                            val formattedArticlesData = FormattedArticlesData()
                            formattedArticlesData.id = id
                            formattedArticlesData.title = title
                            formattedArticlesData.min_to_read = minToRead
                            formattedArticlesData.post_views = postViewCount
                            formattedArticlesData.tag = tag
                            formattedArticlesData.image_url = imageUrl
                            formattedArticlesData.articleTye = Constants.TRENDING_ARTICLES
                            trendingArticlesList.add(formattedArticlesData)
                        }
                    }
                    articlesData[Constants.TRENDING_ARTICLES] = trendingArticlesList
                }
                getSuggestedArticles()
            } catch (ex: Exception) {
                getSuggestedArticles()
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
    }

    private fun getSuggestedArticles() {
        viewModelScope.launch {
            try {
                val response = articlesRepository.getSuggestedArticles()
                if (response.isSuccessful) {
                    val featuredArticleResponse = response.body()
                    val suggestedArticlesList = ArrayList<FormattedArticlesData>()

                    if (featuredArticleResponse != null) {
                        for (item in featuredArticleResponse) {
                            val id = item.ID
                            val title =
                                if (item.post_title.isNotNullOrEmpty()) item.post_title else ""
                            val minToRead =
                                if (item.min_to_read.isNotNullOrEmpty()) item.min_to_read else ""
                            val postViewCount =
                                if (item.post_views.isNotNullOrEmpty()) item.post_views else ""
                            var tag = ""
                            if (item.acf!= null && item.acf !is Boolean) {
                                tag = try {
                                    val categoryName: String = (item.acf as Map<*, *>)["category_name"] as String
                                    if (categoryName.isNotNullOrEmpty()) categoryName else ""
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    ""
                                }
                            }
                            val imageUrl =
                                if (item.featured_image.isNotNullOrEmpty()) item.featured_image else ""

                            val formattedArticlesData = FormattedArticlesData()
                            formattedArticlesData.id = id
                            formattedArticlesData.title = title
                            formattedArticlesData.min_to_read = minToRead
                            formattedArticlesData.post_views = postViewCount
                            formattedArticlesData.tag = tag
                            formattedArticlesData.image_url = imageUrl
                            formattedArticlesData.articleTye = Constants.SUGGESTED_ARTICLES
                            suggestedArticlesList.add(formattedArticlesData)
                        }
                    }
                    articlesData[Constants.SUGGESTED_ARTICLES] = suggestedArticlesList
                }
                notifyAdapter()
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
                notifyAdapter()
            }
        }
    }

    private fun notifyAdapter() {
        getNavigator()?.notifyHomeAdapter(
            allBannerResponse, categoryResponse, allProductsResponse,
            cropResponse,
            storeResponse, companyResponse, cartList, farmResponse, articlesData)
    }
}