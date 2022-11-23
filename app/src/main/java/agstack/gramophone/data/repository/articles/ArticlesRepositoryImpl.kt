package agstack.gramophone.data.repository.articles

import agstack.gramophone.di.ArticlesApiService
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val articleApiService: ArticlesApiService
) : ArticlesRepository {

    override suspend fun getFeaturedArticles(language: String): Response<FeaturedArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getFeaturedArticles(language, SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.session_token)!!)
            response
        }

    override suspend fun getTrendingArticles(language: String): Response<TrendingArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getTrendingArticles(language, SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.session_token)!!)
            response
        }

    override suspend fun getSuggestedArticles(suggestedCrops: String, language: String): Response<SuggestedArticlesResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = articleApiService.getSuggestedArticles(suggestedCrops, language, SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.session_token)!!)
            response
        }
}