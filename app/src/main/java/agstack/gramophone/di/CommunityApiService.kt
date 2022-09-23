package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface CommunityApiService {

    @POST("/api/v2/posts/all")
    suspend fun getCommunityPost(@Body sort: CommunityRequestModel?): Response<CommunityHomeResponseModel>


    @POST("/api/v2/likes/get-likes")
    suspend fun getLikedUsers(@Body likedUsersRequestModel: PostRequestModel?): Response<LikedusersResponseModel>

    @PUT("/api/v2/likes/update-like")
    suspend fun likePost(@Body likedUsersRequestModel: PostRequestModel?): Response<LikePostResponseModel>

    @PUT("/api/v2/posts/update-bookmark")
    suspend fun bookmarkPost(@Body likedUsersRequestModel: PostRequestModel?): Response<BookmarkPostResponse>
}