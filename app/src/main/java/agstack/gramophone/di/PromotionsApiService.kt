package agstack.gramophone.di

import agstack.gramophone.ui.home.subcategory.model.CheckOfferRequest
import agstack.gramophone.ui.home.subcategory.model.CheckOfferResponse
import agstack.gramophone.ui.offerslist.model.OfferListRequestModel
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PromotionsApiService {



    @POST("api/v1/all-offer-list")
    suspend fun getAllOffersList(@Body offerListRequestModel: OfferListRequestModel): Response<PromotionsAllOfferResponse>

    @POST("api/v1/check-promotion-on-product")
    suspend fun checkOfferOnProduct(@Body checkOfferRequest: CheckOfferRequest): Response<CheckOfferResponse>
}