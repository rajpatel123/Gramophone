package agstack.gramophone.retrofit

import agstack.gramophone.splash.model.AppConfig
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): AppConfig

    @GET("users")
    suspend fun getConfig(): AppConfig

    @GET("users")
    suspend fun loginUser(): AppConfig


}