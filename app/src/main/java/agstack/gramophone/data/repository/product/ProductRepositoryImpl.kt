package agstack.gramophone.data.repository.product


import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.articles.SuggestedCropResponse
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.PlaceOrderRequest
import agstack.gramophone.ui.dialog.filter.FilterRequest
import agstack.gramophone.ui.farm.model.*
import agstack.gramophone.ui.farm.model.unit.FarmUnitResponse
import agstack.gramophone.ui.home.product.model.CheckPromotionResponseModel
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.home.view.fragments.gramophone.model.MyGramophoneResponseModel
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.GlobalSearchResponse
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.model.SuggestionsResponse
import agstack.gramophone.ui.tv.model.VideoBookMarkedRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val gramoAppService: GramAppService,
) : ProductRepository {

    override suspend fun getProductData(productMap: ProductData): Response<ProductDataResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val popular = gramoAppService.getProductData(productMap)
            popular
        }


    override suspend fun getProductReviewsData(
        sortBy: String?,
        page: String?,
        productMap: ProductData,
    ): Response<ProductReviewDataResponse> = withContext(
        Dispatchers.IO
    ) {
        val reviews = gramoAppService.getReviewData(sortBy, page, productMap)
        reviews
    }

    override suspend fun addProductReviewsData(productData: ProductData): Response<ProductReviewDataResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.addReviewData(productData)
            response
        }

    override suspend fun updateProductReviewsData(productData: ProductData): Response<ProductReviewDataResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.updateReviewData(productData)
            response
        }

    override suspend fun getRelatedProductsData(productMap: ProductData): Response<RelatedProductResponseData> =
        withContext(
            Dispatchers.IO
        ) {
            val relatedProd = gramoAppService.getRelatedProductsData(productMap)
            relatedProd
        }

    override suspend fun getOffersOnProductData(productMap: ProductData): Response<OffersProductResponseData> =
        withContext(
            Dispatchers.IO
        ) {
            val relatedProd = gramoAppService.getOffersOnProductData(productMap)
            relatedProd
        }

    override suspend fun addToCart(productData: ProductData): Response<CartDataResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val cartData = gramoAppService.addToCart(productData)
            cartData
        }

    override suspend fun getExpertAdvice(productData: ProductData): Response<SuccessStatusResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val expertAdvice = gramoAppService.getExpertAdvice(productData)
            expertAdvice
        }

    override suspend fun updateProductFavorite(productData: ProductData): Response<SuccessStatusResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.updateProductFavorite(productData)
            response
        }

    override suspend fun getCartData(): Response<CartDataResponse> = withContext(
        Dispatchers.IO
    ) {
        val cartData = gramoAppService.getCartData()
        cartData
    }

    override suspend fun removeCartItem(productId: Int): Response<SuccessStatusResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val removedResponse = gramoAppService.removeCartItem(productId)
            removedResponse
        }

    override suspend fun updateCartItem(productData: ProductData): Response<SuccessStatusResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val removedResponse = gramoAppService.updateCartItem(productData)
            removedResponse
        }

    override suspend fun getOrderData(type: String, limit: String, page: String): Response<OrderListResponse> = withContext(
        Dispatchers.IO
    ) {
        val orderData = gramoAppService.getOrderData(type, PageLimitRequest(limit, page))
        orderData
    }

    override suspend fun getOrderDetails(orderDetailRequest: OrderDetailRequest): Response<OrderDetailResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val orderDetails = gramoAppService.getOrderDetails(orderDetailRequest)
            orderDetails
        }

    override suspend fun placeOrder(placeOrderRequest: PlaceOrderRequest): Response<PlaceOrderResponse> = withContext(
        Dispatchers.IO
    ) {
        val placeOrderResponse = gramoAppService.placeOrder(placeOrderRequest)
        placeOrderResponse
    }

    override suspend fun getBanners(): Response<BannerResponse> = withContext(
        Dispatchers.IO
    ) {
        val bannerResponse = gramoAppService.getBanners()
        bannerResponse
    }

    override suspend fun getCategories(): Response<CategoryResponse> = withContext(
        Dispatchers.IO
    ) {
        val categoryResponse = gramoAppService.getCategories()
        categoryResponse
    }

    override suspend fun getCompanies(): Response<CompanyResponse> = withContext(
        Dispatchers.IO
    ) {
        val companiesResponse = gramoAppService.getCompanies()
        companiesResponse
    }

    override suspend fun getStores(): Response<StoreResponse> = withContext(
        Dispatchers.IO
    ) {
        val storeResponse = gramoAppService.getStores()
        storeResponse
    }

    override suspend fun getCrops(): Response<CropResponse> = withContext(
        Dispatchers.IO
    ) {
        val cropResponse = gramoAppService.getCrops()
        cropResponse
    }

    override suspend fun getSubCategories(categoryId: String): Response<SubCategoryResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val subCategoryResponse = gramoAppService.getSubCategory(categoryId)
            subCategoryResponse
        }

    override suspend fun getHomeData(): Response<HomeDataResponse> = withContext(
        Dispatchers.IO
    ) {
        val homeDataResponse = gramoAppService.getHomeData()
        homeDataResponse
    }

    override suspend fun getHelp(
        type: String,
        productData: ProductData,
    ): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.getHelp(type, productData)
        response
    }

    override suspend fun getAllProducts(
        filterRequest: FilterRequest,
    ): Response<AllProductsResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.getAllProducts(filterRequest)
        response
    }

    override suspend fun getFeaturedProducts(pageLimitRequest: PageLimitRequest): Response<AllProductsResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.getFeaturedProduct(pageLimitRequest)
        response
    }

    override suspend fun getStoresFilterData(storeId: String): Response<SubCategoryResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val subCategoryResponse = gramoAppService.getStoresFilterData(storeId)
            subCategoryResponse
        }

    override suspend fun getFarmsData(type : String, farmRequest: FarmRequest): Response<FarmResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.getFarmsData(type, farmRequest)
        response
    }

    override suspend fun addFarm(addFarmRequest: AddFarmRequest): Response<AddFarmResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.addFarm(addFarmRequest)
        response
    }

    override suspend fun getFarmUnits(type: String): Response<FarmUnitResponse> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.getFarmUnits(type)
        response
    }

    override suspend fun checkPromotionOnProduct(verifyPromotionRequestModel: VerifyPromotionRequestModel): Response<CheckPromotionResponseModel> = withContext(
        Dispatchers.IO
    ) {
        val response = gramoAppService.checkPromotionApplicable(verifyPromotionRequestModel)
        response
    }


    override suspend fun getSuggestions(body: SuggestionsRequest): Response<SuggestionsResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.getSuggestions(body)
            response
        }

    override suspend fun searchByKeyword(body: GlobalSearchRequest): Response<GlobalSearchResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.searchByKeyword(body)
            response
        }

    override suspend fun searchByKeywordInCommunity(body: GlobalSearchRequest): Response<GlobalSearchResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.searchByKeywordInCommunity(body)
            response
        }

    override suspend fun addHarvestQues(body: AddHarvestRequest): Response<MyGramophoneResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.addHarvestQues(body)
            response
        }

    override suspend fun bookmarkVideo(body: VideoBookMarkedRequest): Response<SuccessStatusResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.bookmarkVideo(body)
            response
        }

    override suspend fun getSuggestedCrops(): Response<SuggestedCropResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramoAppService.getSuggestedCrops()
            response
        }
}