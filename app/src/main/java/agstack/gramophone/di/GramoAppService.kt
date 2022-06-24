package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegistrerDeviceRquestModel
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface GramoAppService {

    @POST("traders/update-profile-type")
    suspend fun updateProfileType(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    @POST("onboarding/register-device")
    suspend fun getDeviceToken(@Body hashMap:HashMap<Any,Any>): Response<UpdateProfileTypeRes>

    @POST("onboarding/send-otp")
    @JvmSuppressWildcards
    suspend fun sendOTP(@Body hashMap:HashMap<Any,Any>): Response<GenerateOtpResponseModel>


    @POST("onboarding/register-device")
    @JvmSuppressWildcards
    suspend fun getDeviceToken(@Body registrerDeviceRquestModel: RegistrerDeviceRquestModel): Response<RegisterDeviceModel>

    @POST("onboarding/validate-otp")
    @JvmSuppressWildcards
    suspend fun validateOTP(@Body registrerDeviceRquestModel: RegistrerDeviceRquestModel): Response<RegisterDeviceModel>


    @POST("product/get-product-details")
    fun getProductData(@Body hashMap: java.util.HashMap<*, *>?): Response<ProductData>
}