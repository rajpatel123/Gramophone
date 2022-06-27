package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.repository.HomeRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarketFragmentViewModel
@Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<MarketFragmentNavigator>() {

    var productList = ArrayList<ProductData>()


    suspend fun getFeaturedproducts(hashMap: HashMap<Any, Any>) {
        withContext(
            Dispatchers.IO
        ) {
//To get Product Data
            //homeRepository.getProducts(hashMap)
            productList.add(ProductData("1", "Rice"))
            productList.add(ProductData("2", "Cotton"))
            productList.add(ProductData("3", "Maze"))
            productList.add(ProductData("4", "SunFlower"))
            //set received productList in Adapter

            getNavigator()?.setFeaturedProductsAdapter(ProductListAdapter(productList)) {


                getNavigator()?.startProductDetailsActivity(it)

            }


        }
    }

}