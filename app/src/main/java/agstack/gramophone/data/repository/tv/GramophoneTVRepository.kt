package agstack.gramophone.data.repository.tv

import agstack.gramophone.ui.tv.model.ItemsModelResponse
import agstack.gramophone.ui.tv.model.YoutubeModel
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface GramophoneTVRepository {
    suspend fun getPlayLists(
        part: String,
        channelId: String,
        key: String,
        maxResults: Int,
    ): Response<YoutubeModel>

    suspend fun getPlayListsNextPage(
        part: String,
        channelId: String,
        key: String,
        pageToken: String,
        maxResults: Int,
    ): Response<YoutubeModel>

    suspend fun getVideoIds(
        part: String,
        maxResults: Int,
        playListId: String,
        key: String,
    ): Response<ItemsModelResponse>

    suspend fun getVideoIdsNextPage(
        part: String,
        maxResults: Int,
        playListId: String,
        key: String,
        pageToken: String,
    ): Response<ItemsModelResponse>
}