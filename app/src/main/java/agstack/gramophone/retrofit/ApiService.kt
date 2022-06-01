package agstack.gramophone.retrofit

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("onboarding/onboarding-check")
    suspend fun validateOnboarding() : String

    @POST("onboarding/onboarding-check")
    suspend fun getLangaugesList() :List<String>

    @POST("onboarding/send-otp")
    suspend fun loginUser(@Body hashMap: HashMap<String,String>): String

    @POST("onboarding/resend-otp")
     suspend fun resendMobileNo(@Body hashMap: HashMap<*, *>?): String

    @POST("onboarding/update-phone-otp")
    suspend fun sendUpdatedMobileNo(@Body hashMap: HashMap<*, *>?): Boolean

    @PUT("onboarding/update-phone-no")
    suspend fun validateUpdatePhoneOtp(@Body hashMap: HashMap<*, *>?): Boolean

    @POST("onboarding/validate-otp")
    suspend fun validateOtp(@Body hashMap: HashMap<*, *>?): ResponseBody


}