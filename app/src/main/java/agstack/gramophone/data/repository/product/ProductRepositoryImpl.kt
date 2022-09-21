package agstack.gramophone.data.repository.product


import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.cart.model.AddToCartRequest
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
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

    override suspend fun getExpertAdvice(productData: ProductData): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO) {
        val expertAdvice = gramoAppService.getExpertAdvice(productData)
        expertAdvice
    }

    override suspend fun updateProductFavorite(productData: ProductData): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO) {
        val response = gramoAppService.updateProductFavorite(productData)
        response
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

    override suspend fun getBanners(): Response<BannerResponse>  = withContext(
        Dispatchers.IO) {
        val bannerResponse = gramoAppService.getBanners()
        bannerResponse
    }

    override suspend fun getCategories(): Response<CategoryResponse>  = withContext(
        Dispatchers.IO) {
        val categoryResponse = gramoAppService.getCategories()
        categoryResponse
    }

    override suspend fun getCompanies(): Response<CompanyResponse>  = withContext(
        Dispatchers.IO) {
        val companiesResponse = gramoAppService.getCompanies()
        companiesResponse
    }

    override suspend fun getStores(): Response<StoreResponse>  = withContext(
        Dispatchers.IO) {
        val storeResponse = gramoAppService.getStores()
        storeResponse
    }

    override suspend fun getCrops(): Response<CropResponse>  = withContext(
        Dispatchers.IO) {
        val cropResponse = gramoAppService.getCrops()
        cropResponse
    }

    override suspend fun getSubCategories(categoryId: String): Response<SubCategoryResponse>  = withContext(
        Dispatchers.IO) {
        val subCategoryResponse = gramoAppService.getSubCategory(categoryId)
        subCategoryResponse
    }

    override suspend fun getHomeData(): Response<HomeDataResponse>  = withContext(
        Dispatchers.IO) {
        val homeDataResponse = gramoAppService.getHomeData()
        homeDataResponse
    }

    override suspend fun getAllProducts(): Response<AllProductsResponse> {
        TODO("Not yet implemented")
    }
}