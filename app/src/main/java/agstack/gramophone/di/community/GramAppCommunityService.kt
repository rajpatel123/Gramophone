package agstack.gramophone.di.community

import agstack.gramophone.ui.home.view.fragments.market.model.HomeDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface GramAppCommunityService {
    @GET("api/v2/posts/get-trending-post")
    suspend fun getTrendingPost(): Response<HomeDataResponse>
}