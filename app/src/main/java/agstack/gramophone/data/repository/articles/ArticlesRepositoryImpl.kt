package agstack.gramophone.data.repository.articles

import agstack.gramophone.di.ArticlesApiService
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
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


}