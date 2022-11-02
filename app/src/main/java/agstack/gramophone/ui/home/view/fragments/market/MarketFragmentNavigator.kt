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

    fun notifyHomeAdapter(
        allBannerResponse: BannerResponse?, categoryResponse: CategoryResponse?,
        allProductsResponse: AllProductsResponse?, cropResponse: CropResponse?,
        storeResponse: StoreResponse?, companyResponse: CompanyResponse?,
        cartList: List<CartItem>?, farmResponse: FarmResponse?, articlesData: HashMap<String, ArrayList<FormattedArticlesData>>,
        weatherResponse: WeatherResponse?
    )

    fun launchCommunityFragment()
}