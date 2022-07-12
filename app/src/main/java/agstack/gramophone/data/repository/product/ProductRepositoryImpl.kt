package agstack.gramophone.data.repository.product


import agstack.gramophone.di.GramoAppService
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val gramoAppService: GramoAppService
) : ProductRepository {

    override suspend fun getProductData(productMap: HashMap<Any, Any>): Response<ProductData> = withContext(
        Dispatchers.IO) {
        val popular = gramoAppService.getProductData(productMap)
        popular
    }

}