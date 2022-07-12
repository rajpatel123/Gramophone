package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.RegisterDeviceRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import retrofit2.Response
import retrofit2.http.Body
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


    @POST("product/get-product-details")
    fun getProductData(@Body hashMap: java.util.HashMap<*, *>?): Response<ProductData>
}