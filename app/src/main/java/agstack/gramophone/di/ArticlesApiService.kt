package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApiService {

    @GET("wp-json/wp/v2/posts?sticky=1&orderby=featured_post_sorting&_embed&per_page=5")
    suspend fun getFeaturedArticles(): Response<FeaturedArticlesResponse>

    /*@GET("wp-json/gp/v1/popular-posts?embed&limit=5")*/
    @GET("wp-json/gp/v1/popular-posts?embed")
    suspend fun getTrendingArticles(): Response<TrendingArticlesResponse>

    @GET("wp-json/gp/v1/posts-by-field?post_type=post&limit=5")
    suspend fun getSuggestedArticles(@Query("suggested_crops") suggestedCrops: String): Response<SuggestedArticlesResponse>
}