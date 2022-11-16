package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import agstack.gramophone.ui.offerslist.model.PromotionsAllOfferResponse
import agstack.gramophone.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApiService {

    @GET("wp-json/wp/v2/posts?lang=en&sticky=1&orderby=featured_post_sorting&_embed&per_page=5")
    suspend fun getFeaturedArticles(@Query("lang") language: String, @Query(Constants.GP_TOKEN) gpToken: String): Response<FeaturedArticlesResponse>

    @GET("wp-json/wp/v2/posts?lang=en&sticky=0&orderby=date&_embed&per_page=5")
    suspend fun getTrendingArticles(@Query("lang") language: String, @Query(Constants.GP_TOKEN) gpToken: String): Response<TrendingArticlesResponse>

    @GET("wp-json/gp/v1/posts-by-field?post_type=post&limit=5")
    suspend fun getSuggestedArticles(@Query("suggested_crops") suggestedCrops: String, @Query("lang") language: String, @Query(Constants.GP_TOKEN) gpToken: String): Response<SuggestedArticlesResponse>
}