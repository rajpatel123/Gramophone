package agstack.gramophone.di

import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface GramoAppService {

    @POST("onboarding/send-otp")
    suspend fun sendOTP(@Body hashMap:HashMap<Any,Any>): Response<GenerateOtpResponseModel>

}