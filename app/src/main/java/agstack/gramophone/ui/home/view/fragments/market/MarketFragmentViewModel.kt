package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.articles.ArticlesRepository
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.data.repository.weather.WeatherRepository
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.farm.model.FarmRequest
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.LostConnectionActivity
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.ui.weather.model.WeatherResponse
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNotNull
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.amnix.xtension.extensions.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MarketFragmentViewModel
@Inject constructor(
    private val productRepository: ProductRepository,
    private val articlesRepository: ArticlesRepository,
    private val weatherRepository: WeatherRepository,
) : BaseViewModel<MarketFragmentNavigator>() {

    var homeScreenSequenceList: List<String> = ArrayList()
    var allProductsResponse: AllProductsResponse? = null
    var cropResponse: CropResponse? = null
    var storeResponse: StoreResponse? = null
    var companyResponse: CompanyResponse? = null
    var allBannerResponse: BannerResponse? = null
    var categoryResponse: CategoryResponse? = null
    var cartList: List<CartItem>? = arrayListOf()
    var farmResponse: FarmResponse? = null
    var weatherResponse: WeatherResponse? = null
    var articlesData: HashMap<String, ArrayList<FormattedArticlesData>> = HashMap()
    var language: String? = null

    fun showAppTourDialogIfApplicable() {
        if (SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.APP_TOUR_ENABLED) == true
            && SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.APP_TOUR_SKIP_COUNT)!! < 2
        ) {
            getNavigator()?.showAppTourDialog()
        }
    }

    fun getHomeData() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            language =
                if (SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.languageCode)
                        .isNullOrEmpty()
                ) {
                    "en"
                } else {
                    SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.languageCode)!!
                }
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
                        homeScreenSequenceList =
                            response.body()?.gp_api_response_data?.home_screen_sequence!!
                        getNavigator()?.setHomeAdapter(HomeAdapter(homeScreenSequenceList,
                            allBannerResponse,
                            categoryResponse,
                            allProductsResponse,
                            cropResponse,
                            storeResponse,
                            companyResponse,
                            cartList,
                            farmResponse,
                            articlesData,
                            weatherResponse, getNavigator()?.getActivityContext())) {
                            getNavigator()?.launchCommunityFragment()
                        }
                    }
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)
        }

    }

    private fun assignPosition(viewType: String): Int {
        for ((index, item) in homeScreenSequenceList.withIndex()) {
            if (item == viewType) {
                return index
            }
        }
        return homeScreenSequenceList.size - 1
    }

    /*
    * Don't remove the logic for getting banners in below method
    * OR If any discussion happened to change the logic then mention here
    */
    fun getBanners(refreshFromApi: Boolean) {
        if (getNavigator()?.isNetworkAvailable() == true){
            var isRefreshFromApi = refreshFromApi
            viewModelScope.launch {
                try {
                    allBannerResponse = SharedPreferencesHelper.instance?.getParcelable(
                        SharedPreferencesKeys.BANNER_DATA, BannerResponse::class.java
                    )
                    if (allBannerResponse.isNull() || allBannerResponse?.gpApiStatus != Constants.GP_API_STATUS) {
                        isRefreshFromApi = true
                    }
                    if (isRefreshFromApi) {
                        val response = productRepository.getBanners()
                        if (response.isSuccessful && response.body()?.gpApiStatus == Constants.GP_API_STATUS) {
                            allBannerResponse = response.body()
                            SharedPreferencesHelper.instance?.putParcelable(
                                SharedPreferencesKeys.BANNER_DATA,
                                allBannerResponse!!
                            )
                        }
                    }
                    getNavigator()?.notifyBannerItemInserted(allBannerResponse,
                        assignPosition(Constants.HOME_BANNER_1))
                    getNavigator()?.notifyBannerItemInserted(allBannerResponse,
                        assignPosition(Constants.HOME_BANNER_REFERRAL))
                    getNavigator()?.notifyBannerItemInserted(allBannerResponse,
                        assignPosition(Constants.HOME_BANNER_EXCLUSIVE))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }else{
           getNavigator()?.openActivity(LostConnectionActivity::class.java)
        }

    }

    fun getFeaturedProducts() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response =
                        productRepository.getFeaturedProducts(PageLimitRequest("10", "1"))
                    allProductsResponse = response.body()
                    getNavigator()?.notifyFeaturedItemInserted(
                        allProductsResponse,
                        assignPosition(Constants.HOME_FEATURED_PRODUCTS)
                    )
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)
        }
    }

    fun getCategories() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response = productRepository.getCategories()
                    categoryResponse = response.body()
                    getNavigator()?.notifyCategoryItemInserted(categoryResponse,
                        assignPosition(Constants.HOME_SHOP_BY_CATEGORY))
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
          getNavigator()?.openActivity(LostConnectionActivity::class.java)
        }
    }

    fun getCrops() {
        if (getNavigator()?.isNetworkAvailable() == true) {
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
                getNavigator()?.notifyCropItemInserted(cropResponse,
                    assignPosition(Constants.HOME_SHOP_BY_CROP))
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                }
            }
        }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)
        }

    }

    fun getStores() {
        if (getNavigator()?.isNetworkAvailable() == true) {
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
                    getNavigator()?.notifyStoreItemInserted(storeResponse,
                        assignPosition(Constants.HOME_SHOP_BY_STORE))
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    fun getWeatherDetail() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            val weatherRequest = WeatherRequest(null, null)
            viewModelScope.launch {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val response = weatherRepository.getWeatherDetail(weatherRequest)
                        if (response.body()?.gp_api_status.equals(Constants.GP_API_STATUS)
                            && response.body()?.gp_api_response_data.isNotNullOrEmpty()
                            && response.body()?.gp_api_response_data?.get(0).isNotNull()
                        ) {
                            weatherResponse = response.body()
                        }
                        getNavigator()?.notifyWeatherItemInserted(weatherResponse,
                            assignPosition(Constants.HOME_WEATHER))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }

    }

    fun getCompanies() {
        if (getNavigator()?.isNetworkAvailable() == true) {
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
                    getNavigator()?.notifyCompanyItemInserted(companyResponse,
                        assignPosition(Constants.HOME_SHOP_BY_COMPANY))
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    fun getCartProducts() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response = productRepository.getCartData()
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data?.cart_items != null && response.body()?.gp_api_response_data?.cart_items?.size!! > 0
                    ) {
                        cartList = response.body()?.gp_api_response_data?.cart_items
                        SharedPreferencesHelper.instance?.putInteger(
                            SharedPreferencesKeys.CART_ITEM_COUNT, cartList!!.size)
                    }
                    getNavigator()?.notifyCartItemInserted(cartList,
                        assignPosition(Constants.HOME_CART))
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    fun getFarms() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response = productRepository.getFarmsData("active", FarmRequest("3", "1"))
                    if (response.isSuccessful && response.body()?.gp_api_status == Constants.GP_API_STATUS
                        && response.body()?.gp_api_response_data != null
                    ) {
                        farmResponse = response.body()
                    }
                    getNavigator()?.notifyFarmItemInserted(farmResponse,
                        assignPosition(Constants.HOME_FARMS))
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    fun getFeaturedArticles() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response =
                        articlesRepository.getFeaturedArticles(if (language.isNullOrEmpty()) "en" else language!!)
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
                                var videoThumbnailUrl: String? = null
                                if (item.acf != null && item.acf !is Boolean) {
                                    tag = try {
                                        val categoryName: String =
                                            (item.acf as Map<*, *>)["category_name"] as String
                                        if (categoryName.isNotNullOrEmpty()) categoryName else ""
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        ""
                                    }
                                    val videoUrl = try {
                                        val videoLink: String =
                                            (item.acf as Map<*, *>)["video_url"] as String
                                        if (videoLink.isNotNullOrEmpty()) videoLink else ""
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        ""
                                    }
                                    if (videoUrl.isNotNullOrEmpty()) {
                                        val uri: Uri = Uri.parse(videoUrl)
                                        if (uri.lastPathSegment.toString().isNotNullOrEmpty()) {
                                            videoThumbnailUrl =
                                                "https://img.youtube.com/vi/" + uri.lastPathSegment.toString() + "/0.jpg"
                                        }
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
                                formattedArticlesData.videoThumbnailUrl = videoThumbnailUrl
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
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    private fun getTrendingArticles() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val response =
                        articlesRepository.getTrendingArticles(if (language.isNullOrEmpty()) "en" else language!!)
                    if (response.isSuccessful) {
                        val featuredArticleResponse = response.body()
                        var trendingArticlesList = ArrayList<FormattedArticlesData>()

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
                                if (item.acf != null && item.acf !is Boolean) {
                                    tag = try {
                                        if ((item.acf as Map<*, *>)["category_name"].isNotNull()) {
                                            val categoryName: String =
                                                (item.acf as Map<*, *>)["category_name"] as String
                                            if (categoryName.isNotNullOrEmpty()) categoryName else ""
                                        } else ""
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        ""
                                    }
                                }
                                var imageUrl = ""
                                if (item.fimg_url !=null && item.fimg_url !is Boolean) {
                                    imageUrl = item.fimg_url.toString()
                                }

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
                        trendingArticlesList.sortByDescending {
                            it.post_views
                        }
                        articlesData[Constants.TRENDING_ARTICLES] = trendingArticlesList
                    }
                    getSuggestedArticles()
                } catch (ex: Exception) {
                    getSuggestedArticles()
                    when (ex) {
                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.network_failure))
//                    else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                    }
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }

    private fun getSuggestedArticles() {
        if (getNavigator()?.isNetworkAvailable() == true) {
            viewModelScope.launch {
                try {
                    val suggestedCropResponse = productRepository.getSuggestedCrops()
                    if (suggestedCropResponse.isSuccessful && suggestedCropResponse.body()?.gp_api_status == Constants.GP_API_STATUS
                        && suggestedCropResponse.body()?.gp_api_response_data?.crops_suggested != null && suggestedCropResponse.body()?.gp_api_response_data?.crops_suggested?.size!! > 0
                    ) {
                        val list = suggestedCropResponse.body()?.gp_api_response_data?.crops_suggested!!
                        var suggestedCrops: String = ""
                        for (item in list) {
                            suggestedCrops = suggestedCrops + item.crop_name + ","
                        }
                        SharedPreferencesHelper.instance?.putString(SharedPreferencesKeys.SUGGESTED_CROPS, suggestedCrops)
                        if (suggestedCrops.isNotNullOrEmpty()) {
                            viewModelScope.launch {
                                try {
                                    val response =
                                        articlesRepository.getSuggestedArticles(suggestedCrops,
                                            if (language.isNullOrEmpty()) "en" else language!!)
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
                                                if (item.acf != null && item.acf !is Boolean) {
                                                    tag = try {
                                                        val categoryName: String =
                                                            (item.acf as Map<*, *>)["category_name"] as String
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
                                                formattedArticlesData.articleTye =
                                                    Constants.SUGGESTED_ARTICLES
                                                suggestedArticlesList.add(formattedArticlesData)
                                            }
                                        }
                                        articlesData[Constants.SUGGESTED_ARTICLES] =
                                            suggestedArticlesList
                                    }
                                    getNavigator()?.notifyArticleItemInserted(articlesData,
                                        assignPosition(Constants.HOME_ARTICLES))
                                } catch (ex: Exception) {
                                    when (ex) {
                                        is IOException -> getNavigator()?.showToast(getNavigator()?.getMessage(
                                            R.string.network_failure))
                                        else -> getNavigator()?.showToast(getNavigator()?.getMessage(R.string.some_thing_went_wrong))
                                    }
                                    getNavigator()?.notifyArticleItemInserted(articlesData,
                                        assignPosition(Constants.HOME_ARTICLES))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }else{
            getNavigator()?.openActivity(LostConnectionActivity::class.java)

        }
    }
}