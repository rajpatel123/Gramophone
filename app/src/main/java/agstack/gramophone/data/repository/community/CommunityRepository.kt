package agstack.gramophone.data.repository.community

import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.*
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.block.BlockResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.follow.FollowResponseModel
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.comments.model.CommentsResponseModel
import agstack.gramophone.ui.othersporfile.model.CommunityUserPostRequestModel
import agstack.gramophone.ui.othersporfile.model.ProfileDataResponse
import agstack.gramophone.ui.userprofile.model.TestUserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Singleton

@Singleton
interface CommunityRepository {
    suspend fun getCommunityPost(sort: CommunityRequestModel): Response<CommunityHomeResponseModel>

    suspend fun getCommunityPost(sort: CommunityUserPostRequestModel): Response<CommunityHomeResponseModel>

    suspend fun getLikedUsers(sort: PostRequestModel): Response<LikedusersResponseModel>

    suspend fun likePost(post: PostRequestModel): Response<LikePostResponseModel>

    suspend fun bookmarkPost(post: PostRequestModel): Response<BookmarkPostResponse>

    suspend fun pinPost(post: PostRequestModel): Response<BlockResponseModel>

    suspend fun getPostDetails(post: String): Response<PostDetailResponseModel>

    suspend fun getPostComments(post: PostRequestModel): Response<CommentsResponseModel>

    suspend fun deletePost(post: String): Response<CommentsResponseModel>

    suspend fun deletePostComment(post: PostRequestModel): Response<CommentsResponseModel>

    suspend fun reportPost(post: ReportUserRequestModel): Response<CommentsResponseModel>

    suspend fun blockUser(post: BlockUserRequestModel): Response<BlockResponseModel>

    suspend fun followPost(post: FollowRequestModel): Response<FollowResponseModel>

    suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part): Response<TestUserModel>

    suspend fun getProfileData(uuid:String): Response<ProfileDataResponse>
}