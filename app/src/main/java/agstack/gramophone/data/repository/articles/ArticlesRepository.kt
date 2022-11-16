package agstack.gramophone.data.repository.articles

import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface ArticlesRepository {
    suspend fun getFeaturedArticles(): Response<FeaturedArticlesResponse>

    suspend fun getTrendingArticles(): Response<TrendingArticlesResponse>

    suspend fun getSuggestedArticles(suggestedCrops: String): Response<SuggestedArticlesResponse>
}