package agstack.gramophone.di

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
    suspend fun sendOTP(@Body hashMap:HashMap<Any,Any>): Response<GenerateOtpResponseModel>

}