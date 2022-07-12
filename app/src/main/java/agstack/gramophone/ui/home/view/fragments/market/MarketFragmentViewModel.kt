package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnboardingRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MarketFragmentViewModel
@Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : BaseViewModel<MarketFragmentNavigator>() {

    var productList = ArrayList<ProductData>()


    suspend fun getFeaturedproducts(hashMap: HashMap<Any, Any>) {
        withContext(
            Dispatchers.IO
        ) {
//To get Product Data
            //homeRepository.getProducts(hashMap)
            var productImagesList = ArrayList<String>()
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productImagesList.add("https://s3.ap-south-1.amazonaws.com/gramophone-webapps-dev/product_erp_images/1647503375.jpg")
            productList.add(
                ProductData(700322)
            )
            productList.add(
                ProductData(700322)
            )
            productList.add(
                ProductData(700322)
            )
            productList.add(
                ProductData(700322)
            )
                        //set received productList in Adapter

                        getNavigator ()?.setFeaturedProductsAdapter(ProductListAdapter(productList)) {


                    getNavigator()?.startProductDetailsActivity(it)

                }


        }
    }

}