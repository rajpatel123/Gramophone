package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegisterDeviceRequestModel
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface GramoAppService {

    @POST("api/v5/onboarding/app-initiate")
    suspend fun initiateApp(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    @POST("traders/update-profile-type")
    suspend fun updateProfileType(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    // onbg service
    @POST("onboarding/register-device")
    suspend fun getDeviceToken(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    @POST("api/v5/onboarding/send-otp")
    @JvmSuppressWildcards
    suspend fun sendOTP(@Body hashMap:HashMap<Any,Any>): Response<GenerateOtpResponseModel>


    @POST("onboarding/register-device")
    @JvmSuppressWildcards
    suspend fun getDeviceToken(@Body registrerDeviceRquestModel: RegisterDeviceRequestModel): Response<RegisterDeviceModel>

    @POST("onboarding/validate-otp")
    @JvmSuppressWildcards
    suspend fun validateOTP(@Body registrerDeviceRquestModel: RegisterDeviceRequestModel): Response<RegisterDeviceModel>


    @POST("product/get-product-details")
    fun getProductData(@Body hashMap: java.util.HashMap<*, *>?): Response<ProductData>
}