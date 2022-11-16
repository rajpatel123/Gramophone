package agstack.gramophone.data.repository.articles

import agstack.gramophone.di.ArticlesApiService
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val articleApiService: ArticlesApiService
) : ArticlesRepository {

    override suspend fun getFeaturedArticles(): Response<FeaturedArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getFeaturedArticles()
            response
        }

    override suspend fun getTrendingArticles(): Response<TrendingArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getTrendingArticles()
            response
        }

    override suspend fun getSuggestedArticles(suggestedCrops: String): Response<SuggestedArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getSuggestedArticles(suggestedCrops)
            response
        }
}