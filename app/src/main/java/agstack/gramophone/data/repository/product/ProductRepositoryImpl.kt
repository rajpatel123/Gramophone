package agstack.gramophone.data.repository.product


import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.cart.model.AddToCartRequest
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
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

    override suspend fun addToCart(addToCartRequest: AddToCartRequest): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO) {
        val addToCartResponse = gramoAppService.addToCart(addToCartRequest)
        addToCartResponse
    }

    override suspend fun getCartData(): Response<CartDataResponse> = withContext(
            Dispatchers.IO) {
            val cartData = gramoAppService.getCartData()
            cartData
        }

    override suspend fun removeCartItem(productId: Int): Response<SuccessStatusResponse>  = withContext(
        Dispatchers.IO) {
        val removedResponse = gramoAppService.removeCartItem(productId)
        removedResponse
    }

    override suspend fun getOrderData(type: String): Response<OrderListResponse>  = withContext(
        Dispatchers.IO) {
        val orderData = gramoAppService.getOrderData(type)
        orderData
    }

    override suspend fun getOrderDetails(orderDetailRequest: OrderDetailRequest): Response<OrderDetailResponse>  = withContext(
        Dispatchers.IO) {
        val orderDetails = gramoAppService.getOrderDetails(orderDetailRequest)
        orderDetails
    }

    override suspend fun placeOrder(): Response<PlaceOrderResponse>  = withContext(
        Dispatchers.IO) {
        val placeOrderResponse = gramoAppService.placeOrder()
        placeOrderResponse
    }
}