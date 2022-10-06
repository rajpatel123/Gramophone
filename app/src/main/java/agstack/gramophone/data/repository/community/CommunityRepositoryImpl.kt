package agstack.gramophone.data.repository.community

import agstack.gramophone.di.CommunityApiService
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val communityApiService: CommunityApiService
) : CommunityRepository {

    override suspend fun getCommunityPost(sort: CommunityRequestModel): Response<CommunityHomeResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getCommunityPost(sort)
            appData
        }

    override suspend fun getCommunityPost(sort: CommunityUserPostRequestModel): Response<CommunityHomeResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getCommunityPost(sort)
            appData
        }

    override suspend fun getLikedUsers(likedUsersRequestModel: PostRequestModel): Response<LikedusersResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getLikedUsers(likedUsersRequestModel)
            appData
        }


    override suspend fun likePost(postResponseModel: PostRequestModel): Response<LikePostResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.likePost(postResponseModel)
            appData
        }


    override suspend fun bookmarkPost(postResponseModel: PostRequestModel): Response<BookmarkPostResponse> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.bookmarkPost(postResponseModel)
            appData
        }

    override suspend fun pinPost(postResponseModel: PostRequestModel): Response<BlockResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.pinPost(postResponseModel)
            appData
        }


    override suspend fun getPostDetails(id: String): Response<PostDetailResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getPostDetails(id)
            appData
        }

    override suspend fun getPostComments(id: PostRequestModel): Response<CommentsResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getPostComments(id)
            appData
        }

    override suspend fun deletePost(id: String): Response<CommentsResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.deletePost(id)
            appData
        }

    override suspend fun deletePostComment(id: PostRequestModel): Response<CommentsResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.deletePostComment(id)
            appData
        }


    override suspend fun reportPost(post: ReportUserRequestModel): Response<CommentsResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.reportPost(post)
            appData
        }

    override suspend fun blockUser(post: BlockUserRequestModel): Response<BlockResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.blockUser(post)
            appData
        }

    override suspend fun followPost(post: FollowRequestModel): Response<FollowResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.followPost(post)
            appData
        }


    override suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part): Response<TestUserModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.updateUserProfileImage(postImage)
        userData
    }

    override suspend fun getProfileData(uuid: String): Response<ProfileDataResponse> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.getProfileData(uuid)
        userData
    }


}