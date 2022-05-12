package agstack.gramophone.retrofit

import agstack.gramophone.ui.language.model.AppConfig
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): AppConfig

}