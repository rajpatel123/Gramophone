package agstack.gramophone.retrofit

import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun loginUser(): String




}