package agstack.gramophone.data.repository.promotions

import agstack.gramophone.di.PromotionsApiService
import agstack.gramophone.ui.home.subcategory.model.CheckOfferRequest
import agstack.gramophone.ui.home.subcategory.model.CheckOfferResponse
import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromotionsRepositoryImpl @Inject constructor(
    private val promotionsApiService: PromotionsApiService
) : PromotionsRepository {



  override suspend fun getAllOffersList(offerListRequestModel: OfferListRequestModel): Response<PromotionsAllOfferResponse> = withContext(
        Dispatchers.IO) {
        val offersData = promotionsApiService.getAllOffersList(offerListRequestModel)
      offersData
    }

    override suspend fun checkOfferOnProduct(checkOfferRequest: CheckOfferRequest): Response<CheckOfferResponse> = withContext(
        Dispatchers.IO) {
        val offersData = promotionsApiService.checkOfferOnProduct(checkOfferRequest)
        offersData
    }

}
