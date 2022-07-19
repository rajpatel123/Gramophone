package agstack.gramophone.data.repository.product


import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductDataResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface ProductRepository {

    suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse>

    suspend fun getProductReviewsData(productMap: ProductData):Response<ProductReviewDataResponse>

    suspend fun getRelatedProductsData(productMap: ProductData):Response<RelatedProductResponseData>

    suspend fun getOffersOnProductData(productMap: ProductData):Response<OffersProductResponseData>



    suspend fun getCartData(): Response<CartDataResponse>
}