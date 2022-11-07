package agstack.gramophone.di

import agstack.gramophone.ui.tv.model.ItemsModelResponse
import agstack.gramophone.ui.tv.model.YoutubeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GramophoneTVApiService {
    @GET("playlists?")
    fun getPlayLists(
        @Query("part") part: String?,
        @Query("channelId") channelId: String?,
        @Query("key") key: String?,
        @Query("maxResults") maxResults: Int,
    ): Call<YoutubeModel>

    @GET("playlists?")
    fun getPlayListsNextPage(
        @Query("part") part: String?,
        @Query("channelId") channelId: String?,
        @Query("key") key: String?,
        @Query("pageToken") pageToken: String?,
        @Query("maxResults") maxResults: Int,
    ): Call<YoutubeModel>

    @GET("playlistItems?")
    fun getVideoIds(
        @Query("part") part: String?, @Query("maxResults") maxResults: Int,
        @Query("playlistId") playlistId: String?, @Query("key") key: String?,
    ): Call<ItemsModelResponse>

    @GET("playlistItems?")
    fun getVideoIdsNextPage(
        @Query("part") part: String?,
        @Query("playlistId") playlistId: String?,
        @Query("key") key: String?,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int,
    ): Call<ItemsModelResponse>
}