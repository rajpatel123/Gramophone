package agstack.gramophone.retrofit

import agstack.gramophone.language.model.AppConfig
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): AppConfig

}