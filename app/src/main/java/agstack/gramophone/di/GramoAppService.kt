package agstack.gramophone.di

import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegistrerDeviceRquestModel
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface GramoAppService {

    @POST("onboarding/send-otp")
    @JvmSuppressWildcards
    suspend fun sendOTP(@Body hashMap:HashMap<Any,Any>): Response<GenerateOtpResponseModel>


    @POST("onboarding/register-device")
    @JvmSuppressWildcards
    suspend fun getDeviceToken(@Body registrerDeviceRquestModel: RegistrerDeviceRquestModel): Response<RegisterDeviceModel>

    @POST("onboarding/validate-otp")
    @JvmSuppressWildcards
    suspend fun validateOTP(@Body registrerDeviceRquestModel: RegistrerDeviceRquestModel): Response<RegisterDeviceModel>

}