package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.adapter.HomeAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.weather.model.WeatherResponse

interface MarketFragmentNavigator : BaseNavigator {
    fun showAppTourDialog()

    fun setHomeAdapter(
        adapter: HomeAdapter,
        onItemClick: (String) -> Unit,
    )

    fun notifyBannerItemInserted(allBannerResponse: BannerResponse?, position: Int)

    fun notifyCategoryItemInserted(categoryResponse: CategoryResponse?, position: Int)

    fun notifyFeaturedItemInserted(allProductsResponse: AllProductsResponse?, position: Int)

    fun notifyCropItemInserted(cropResponse: CropResponse?, position: Int)

    fun notifyStoreItemInserted(storeResponse: StoreResponse?, position: Int)

    fun notifyCompanyItemInserted(companyResponse: CompanyResponse?, position: Int)

    fun notifyCartItemInserted(cartList: List<CartItem>?, position: Int)

    fun notifyFarmItemInserted(farmResponse: FarmResponse?, position: Int)

    fun notifyArticleItemInserted(articlesData: HashMap<String, ArrayList<FormattedArticlesData>>, position: Int)

    fun notifyWeatherItemInserted(weatherResponse: WeatherResponse?, position: Int)

    fun launchCommunityFragment()
}