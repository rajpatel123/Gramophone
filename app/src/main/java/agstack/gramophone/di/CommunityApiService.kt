package agstack.gramophone.di

import agstack.gramophone.ui.comments.model.CommentsResponseModel
import agstack.gramophone.ui.comments.model.DeleteCommentResponseModel
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.comments.model.sendcomment.SendCommentResponseModel
import agstack.gramophone.ui.createnewpost.view.model.create.CreatePostResponseModel
import agstack.gramophone.ui.followings.model.FollowerResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.pin.UpdatePinResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.QuizPollResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.reportpost.ReportUserPostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.*
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.block.BlockResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.follow.FollowResponseModel
import agstack.gramophone.ui.othersporfile.model.CommunityUserPostRequestModel
import agstack.gramophone.ui.othersporfile.model.ProfileDataResponse
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.UnblockRequestModel
import agstack.gramophone.ui.userprofile.model.TestUserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface CommunityApiService {

    @POST("/api/v2/posts/all")
    suspend fun getCommunityPost(@Body sort: CommunityRequestModel?): Response<CommunityHomeResponseModel>

    @POST("/api/v2/posts/all")
    suspend fun getCommunityPost(@Body sort: CommunityUserPostRequestModel?): Response<CommunityHomeResponseModel>

    @POST("/api/v2/likes/get-likes")
    suspend fun getLikedUsers(@Body likedUsersRequestModel: PostRequestModel?): Response<LikedusersResponseModel>

    @PUT("/api/v2/likes/update-like")
    suspend fun likePost(@Body likedUsersRequestModel: PostRequestModel?): Response<LikePostResponseModel>

    @PUT("/api/v2/posts/update-bookmark")
    suspend fun bookmarkPost(@Body likedUsersRequestModel: PostRequestModel?): Response<BookmarkPostResponse>

    @PUT("/api/v2/posts/update-pin")
    suspend fun pinPost(@Body likedUsersRequestModel: PostRequestModel?): Response<UpdatePinResponseModel>

    @GET("/api/v2/posts/{id}")
    suspend fun getPostDetails(@Path("id") id: String): Response<PostDetailResponseModel>

    @POST("/api/v2/comments/get-comments")//change response model
    suspend fun getPostComments(@Body post: GetCommentRequestModel): Response<CommentsResponseModel>

    @DELETE("/api/v2/posts/delete-post/{id}")//change response model
    suspend fun deletePost(@Path("id") id: String): Response<JSONObject>

    @DELETE("/api/v2/comments/delete-comment")//change response model
    suspend fun deletePostComment(@Body post: PostRequestModel): Response<CommentsResponseModel>

    @POST("/api/v2/posts/add-complain")//change response model
    suspend fun reportPost(@Body post: ReportUserRequestModel): Response<ReportUserPostResponseModel>

    @PUT("/api/v2/posts/update-blocked-user")
    suspend fun blockUser(@Body post: BlockUserRequestModel): Response<BlockResponseModel>


    @PUT("/api/v2/posts/update-blocked-user")
    suspend fun unBlockUser(@Body post: UnblockRequestModel): Response<BlockResponseModel>

    @PUT("/api/v2/profiles/update-follow")
    suspend fun followPost(@Body post: FollowRequestModel): Response<FollowResponseModel>

    @Multipart
    @PUT("api/v2/profiles")
    suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part): Response<TestUserModel>

    @GET("/api/v2/profiles/{id}")
    suspend fun getProfileData(@Path("id") id: String): Response<ProfileDataResponse>


    @Multipart
    @PUT("/api/v2/comments/update-comment")
    suspend fun updateComment(
        @Part("postId") postId: RequestBody,
        @Part("commentId") commentId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part postImage: MultipartBody.Part
    ): Response<SendCommentResponseModel>

    @Multipart
    @POST("/api/v2/comments/add-comment")
    suspend fun postComment(
        @Part("postId") postId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part postImage: MultipartBody.Part
    ): Response<SendCommentResponseModel>
    @Multipart
    @POST("/api/v2/comments/add-comment")
    suspend fun postComment(
        @Part("postId") postId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("tags") tags: RequestBody,
    ): Response<SendCommentResponseModel>

    @Multipart
    @PUT(" /api/v2/comments/update-comment")
    suspend fun updateComment(
        @Part("postId") postId: RequestBody,
        @Part("commentId") commentId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("tags") tags: RequestBody,
    ): Response<SendCommentResponseModel>

    @Multipart
    @POST("/api/v2/posts/create-post")
    suspend fun createPost(
        @Part image_1: MultipartBody.Part?,
        @Part image_2: MultipartBody.Part?,
        @Part image_3: MultipartBody.Part?,
        @Part("description") text: RequestBody?,
        @Part("tags") tags: RequestBody?,
    ): Response<CreatePostResponseModel>


    @Multipart
    @PUT("/api/v2/posts/update-post")
    suspend fun updatePost(
        @Part image_1: MultipartBody.Part?,
        @Part image_2: MultipartBody.Part?,
        @Part image_3: MultipartBody.Part?,
        @Part("description") text: RequestBody?,
        @Part("tags") tags: RequestBody?,
        @Part("removedImages") removedImages: RequestBody?,
        @Part("postId") postId: RequestBody?
    ): Response<CreatePostResponseModel>

    @Multipart
    @PUT("/api/v2/posts/update-post")
    suspend fun updatePost(
        @Part("postId") postId: RequestBody?,
        @Part("tags") tags: RequestBody?,
        @Part("farmArea") farmArea: RequestBody?,
        @Part("showingDate") showingDate: RequestBody?,
    ): Response<CreatePostResponseModel>

    @GET("/api/v2/posts/get-blocked-user")
    suspend fun getBlockedUsersList(): Response<BlockedUsersListResponseModel>


    @DELETE("/api/v2/comments/delete-comment/{postId}/{commentId}")
    suspend fun deleteComment(
        @Path("postId") postID: String,
        @Path("commentId") commentId: String
    ): Response<DeleteCommentResponseModel>


    @POST("/api/v2/profiles/get-followings")
    suspend fun getFollowings(@Body followRequestModel: FollowRequestModel): Response<FollowerResponseModel>


    @POST("/api/v2/profiles/get-followers")
    suspend fun getFollowers(@Body followRequestModel: FollowRequestModel): Response<FollowerResponseModel>




}