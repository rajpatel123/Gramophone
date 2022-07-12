package agstack.gramophone.data.repository.product


import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface ProductRepository {

    suspend fun getProductData(productMap: HashMap<Any, Any>): Response<ProductData>

    suspend fun getCartData(): Response<ProductData>
}