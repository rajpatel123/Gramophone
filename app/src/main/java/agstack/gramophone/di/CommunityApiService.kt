package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.postdetails.model.comments.CommentsResponseModel
import retrofit2.Response
import retrofit2.http.*

interface CommunityApiService {

    @POST("/api/v2/posts/all")
    suspend fun getCommunityPost(@Body sort: CommunityRequestModel?): Response<CommunityHomeResponseModel>


    @POST("/api/v2/likes/get-likes")
    suspend fun getLikedUsers(@Body likedUsersRequestModel: PostRequestModel?): Response<LikedusersResponseModel>

    @PUT("/api/v2/likes/update-like")
    suspend fun likePost(@Body likedUsersRequestModel: PostRequestModel?): Response<LikePostResponseModel>

    @PUT("/api/v2/posts/update-bookmark")
    suspend fun bookmarkPost(@Body likedUsersRequestModel: PostRequestModel?): Response<BookmarkPostResponse>

    @PUT("/api/v2/posts/update-pin")
    suspend fun pinPost(@Body likedUsersRequestModel: PostRequestModel?): Response<BookmarkPostResponse>

    @GET("/api/v2/posts/{id}")
    suspend fun getPostDetails(@Path("id") id: String): Response<PostDetailResponseModel>

    @POST("/api/v2/comments/get-comments")
    suspend fun getPostComments(@Body post: PostRequestModel): Response<CommentsResponseModel>
}