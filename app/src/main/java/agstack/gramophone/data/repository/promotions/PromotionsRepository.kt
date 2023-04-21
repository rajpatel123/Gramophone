package agstack.gramophone.data.repository.promotions

import agstack.gramophone.ui.offerslist.model.OfferDetailRequestModel
import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface PromotionsRepository {

    suspend fun getAllOffersList(offerListRequestModel: OfferListRequestModel): Response<PromotionsAllOfferResponse>

    suspend fun getOfferDetails(offerDetailRequestModel: OfferDetailRequestModel): Response<PromotionsAllOfferResponse>

}