package agstack.gramophone.di

import retrofit2.Response
import retrofit2.http.GET

interface CommunityApiService {

    @GET("api/v2/posts/get-trending-post")
    suspend fun getCommunityPost(): Response<String>
}