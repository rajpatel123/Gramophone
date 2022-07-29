package agstack.gramophone.data.repository.product


import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.RemoveCartItemResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface ProductRepository {

    suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse>

    suspend fun getProductReviewsData(sortBy:String?,page:String?,productMap: ProductData):Response<ProductReviewDataResponse>

    suspend fun getRelatedProductsData(productMap: ProductData):Response<RelatedProductResponseData>

    suspend fun getOffersOnProductData(productMap: ProductData):Response<OffersProductResponseData>

    suspend fun getCartData(): Response<CartDataResponse>

    suspend fun removeCartItem(productData: ProductData): Response<RemoveCartItemResponse>

    suspend fun getOrderData(type: String): Response<OrderListResponse>

    suspend fun getOrderDetails(orderDetailRequest: OrderDetailRequest): Response<OrderDetailResponse>
}