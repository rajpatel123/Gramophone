package agstack.gramophone.data.repository.tv

import agstack.gramophone.di.ArticlesApiService
import agstack.gramophone.di.GramophoneTVApiService
import agstack.gramophone.ui.home.view.fragments.market.model.FeaturedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.SuggestedArticlesResponse
import agstack.gramophone.ui.home.view.fragments.market.model.TrendingArticlesResponse
import agstack.gramophone.ui.tv.model.ItemsModelResponse
import agstack.gramophone.ui.tv.model.YoutubeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GramophoneTVRepositoryImpl @Inject constructor(
    private val gramophoneTVApiService: GramophoneTVApiService
) : GramophoneTVRepository {

    override suspend fun getPlayLists(
        part: String,
        channelId: String,
        key: String,
        maxResults: Int,
    ): Response<YoutubeModel> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramophoneTVApiService.getPlayLists(part, channelId, key, maxResults)
            response
        }

    override suspend fun getPlayListsNextPage(
        part: String,
        channelId: String,
        key: String,
        pageToken: String,
        maxResults: Int,
    ): Response<YoutubeModel> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramophoneTVApiService.getPlayListsNextPage(part, channelId, key, pageToken, maxResults)
            response
        }

    override suspend fun getVideoIds(
        part: String,
        maxResults: Int,
        playListId: String,
        key: String,
    ): Response<ItemsModelResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramophoneTVApiService.getVideoIds(part, maxResults, playListId, key)
            response
        }

    override suspend fun getVideoIdsNextPage(
        part: String,
        maxResults: Int,
        playListId: String,
        key: String,
        pageToken: String,
    ): Response<ItemsModelResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramophoneTVApiService.getVideoIdsNextPage(part, playListId, key, pageToken, maxResults)
            response
        }
}