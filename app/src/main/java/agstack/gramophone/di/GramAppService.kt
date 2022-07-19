package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.RemoveCartItemResponse
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductDataResponse
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST


interface GramAppService {

    @POST("traders/update-profile-type")
    suspend fun updateProfileType(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    @POST("api/v5/onboarding/app-initiate")
    @JvmSuppressWildcards
    suspend fun getInitialData(@Body initiateAppDataRequestModel:  InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel>

    @POST("api/v5/onboarding/send-otp")
    @JvmSuppressWildcards
    suspend fun sendOTP(@Body sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>


    @POST("api/v5/onboarding/validate-otp")
    @JvmSuppressWildcards
    suspend fun validateOTP(@Body validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel>



    @POST("api/v5/product/get-product-detail")
    suspend fun getProductData(@Body productData: ProductData): Response<ProductDataResponse>

    @POST("api/v5/review/get-review")
    suspend fun getReviewData(@Body productData: ProductData): Response<ProductReviewDataResponse>

    @POST("api/v5/product/get-related-product")
    suspend fun getRelatedProductsData(@Body productData: ProductData): Response<RelatedProductResponseData>


    @POST("api/v5/product/get-promotion")
    suspend fun getOffersOnProductData(@Body productData: ProductData): Response<OffersProductResponseData>

    @GET("api/v5/cart/get-cart")
    @JvmSuppressWildcards
    suspend fun getCartData(): Response<CartDataResponse>

    @DELETE("api/v5/cart/remove-from-cart")
    @JvmSuppressWildcards
    suspend fun removeCartItem(): Response<RemoveCartItemResponse>
}