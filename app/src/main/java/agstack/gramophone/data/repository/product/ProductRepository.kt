package agstack.gramophone.data.repository.product


import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.ui.cart.model.AddToCartRequest
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface ProductRepository {

    suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse>

    suspend fun getProductReviewsData(sortBy:String?,page:String?,productMap: ProductData):Response<ProductReviewDataResponse>
    suspend fun addProductReviewsData(productData:ProductData):Response<ProductReviewDataResponse>

    suspend fun updateProductReviewsData(productData:ProductData):Response<ProductReviewDataResponse>

    suspend fun getRelatedProductsData(productMap: ProductData):Response<RelatedProductResponseData>

    suspend fun getOffersOnProductData(productMap: ProductData):Response<OffersProductResponseData>

    suspend fun addToCart(addToCartRequest: AddToCartRequest): Response<SuccessStatusResponse>

    suspend fun addToCart(productData:ProductData):Response<CartDataResponse>

    suspend fun updateProductFavorite(productData:ProductData):Response<SuccessStatusResponse>

    suspend fun getCartData(): Response<CartDataResponse>

    suspend fun removeCartItem(productId: Int): Response<SuccessStatusResponse>

    suspend fun getOrderData(type: String): Response<OrderListResponse>

    suspend fun getOrderDetails(orderDetailRequest: OrderDetailRequest): Response<OrderDetailResponse>

    suspend fun placeOrder(): Response<PlaceOrderResponse>

    suspend fun getExpertAdvice(productData:ProductData):Response<SuccessStatusResponse>

    suspend fun getCategories():Response<CategoryResponse>

    suspend fun getCompanies():Response<CompanyResponse>

    suspend fun getStores():Response<StoreResponse>

    suspend fun getCrops():Response<CropResponse>

    suspend fun getSubCategories(categoryId: String):Response<SubCategoryResponse>
}