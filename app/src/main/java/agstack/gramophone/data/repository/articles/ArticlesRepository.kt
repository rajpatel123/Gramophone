package agstack.gramophone.data.repository.articles

import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface ArticlesRepository {
    suspend fun getFeaturedArticles(language: String): Response<FeaturedArticlesResponse>

    suspend fun getTrendingArticles(language: String): Response<TrendingArticlesResponse>

    suspend fun getSuggestedArticles(suggestedCrops: String, language: String): Response<SuggestedArticlesResponse>
}