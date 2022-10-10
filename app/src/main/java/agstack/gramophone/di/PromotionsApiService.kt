package agstack.gramophone.di

import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PromotionsApiService {

    @POST("api/v1/all-offer-list")
    suspend fun getAllOffersList(@Body offerListRequestModel: OfferListRequestModel): Response<PromotionsAllOfferResponse>

}