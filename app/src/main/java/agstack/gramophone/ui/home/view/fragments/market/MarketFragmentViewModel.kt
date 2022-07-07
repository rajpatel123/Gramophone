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
           var productImagesList =ArrayList<String> ()
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productList.add(ProductData("1", "Rice",productImagesList))
            productList.add(ProductData("2", "Cotton",productImagesList))
            productList.add(ProductData("3", "Maze",productImagesList))
            productList.add(ProductData("4", "SunFlower",productImagesList))
            //set received productList in Adapter

            getNavigator()?.setFeaturedProductsAdapter(ProductListAdapter(productList)) {


                getNavigator()?.startProductDetailsActivity(it)

            }


        }
    }

}