package agstack.gramophone.data.repository.product


import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService
) : ProductRepository {

    override suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse> = withContext(
        Dispatchers.IO) {
        val popular = gramAppService.getProductData(productMap)
        popular
    }

    override suspend fun getCartData(): Response<CartDataResponse> = withContext(
        Dispatchers.IO) {
        val cartData = gramAppService.getCartData()
        cartData
    }

}