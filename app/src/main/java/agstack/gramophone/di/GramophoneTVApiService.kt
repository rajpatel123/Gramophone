package agstack.gramophone.di

import agstack.gramophone.ui.tv.model.YoutubePlayListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GramophoneTVApiService {
    @GET("playlists?")
    fun getPlayLists(
        @Query("part") part: String?,
        @Query("channelId") channelId: String?,
        @Query("key") key: String?,
        @Query("maxResults") maxResults: Int,
    ): Call<YoutubePlayListResponse>
}