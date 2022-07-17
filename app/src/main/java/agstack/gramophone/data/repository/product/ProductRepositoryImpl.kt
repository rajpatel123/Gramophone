package agstack.gramophone.data.repository.product


import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductDataResponse
import agstack.gramophone.ui.home.view.fragments.market.model.ProductReviewDataResponse
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


    override suspend fun getProductReviewsData(productMap: ProductData): Response<ProductReviewDataResponse> = withContext(
        Dispatchers.IO) {
        val reviews = gramoAppService.getReviewData(productMap)
        reviews
    }

}