package agstack.gramophone.data.repository.product


import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.RemoveCartItemResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.OrderListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val gramoAppService: GramAppService
) : ProductRepository {

    override suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse> = withContext(
        Dispatchers.IO) {
        val popular = gramoAppService.getProductData(productMap)
        popular
    }


    override suspend fun getProductReviewsData(sortBy:String?,page:String?,productMap: ProductData): Response<ProductReviewDataResponse> = withContext(
        Dispatchers.IO) {
        val reviews = gramoAppService.getReviewData(sortBy,page,productMap)
        reviews
    }

    override suspend fun addProductReviewsData(productData: ProductData): Response<ProductReviewDataResponse> = withContext(
        Dispatchers.IO) {
        val response = gramoAppService.addReviewData(productData)
        response
    }
    override suspend fun updateProductReviewsData(productData: ProductData): Response<ProductReviewDataResponse> = withContext(
    Dispatchers.IO) {
        val response = gramoAppService.updateReviewData(productData)
        response
    }

    override suspend fun getRelatedProductsData(productMap: ProductData): Response<RelatedProductResponseData> = withContext(
    Dispatchers.IO) {
        val relatedProd = gramoAppService.getRelatedProductsData(productMap)
        relatedProd
    }

    override suspend fun getOffersOnProductData(productMap: ProductData): Response<OffersProductResponseData> = withContext(
        Dispatchers.IO) {
        val relatedProd = gramoAppService.getOffersOnProductData(productMap)
        relatedProd
    }

    override suspend fun addToCart(productData: ProductData): Response<CartDataResponse> = withContext(
    Dispatchers.IO) {
        val cartData = gramoAppService.addToCart(productData)
        cartData
    }

    override suspend fun getCartData(): Response<CartDataResponse> = withContext(
            Dispatchers.IO) {
            val cartData = gramoAppService.getCartData()
            cartData
        }

    override suspend fun removeCartItem(productData: ProductData): Response<RemoveCartItemResponse>  = withContext(
        Dispatchers.IO) {
        val removedResponse = gramoAppService.removeCartItem(productData)
        removedResponse
    }

    override suspend fun getOrderData(type: String): Response<OrderListResponse>  = withContext(
        Dispatchers.IO) {
        val orderData = gramoAppService.getOrderData(type)
        orderData
    }
}