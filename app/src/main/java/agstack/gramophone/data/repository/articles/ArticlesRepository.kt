package agstack.gramophone.data.repository.articles

import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface ArticlesRepository {
    suspend fun getFeaturedArticles(): Response<FeaturedArticlesResponse>


}