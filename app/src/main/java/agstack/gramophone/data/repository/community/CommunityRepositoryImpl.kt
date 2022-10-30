package agstack.gramophone.data.repository.community

import agstack.gramophone.di.CommunityApiService
import agstack.gramophone.ui.comments.model.CommentsResponseModel
import agstack.gramophone.ui.comments.model.DeleteCommentResponseModel
import agstack.gramophone.ui.comments.model.sendcomment.GetCommentRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.*
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.block.BlockResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.follow.FollowResponseModel
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.comments.model.sendcomment.SendCommentResponseModel
import agstack.gramophone.ui.createnewpost.view.model.create.CreatePostResponseModel
import agstack.gramophone.ui.followings.model.FollowerResponseModel
import agstack.gramophone.ui.othersporfile.model.CommunityUserPostRequestModel
import agstack.gramophone.ui.othersporfile.model.ProfileDataResponse
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.UnblockRequestModel
import agstack.gramophone.ui.userprofile.model.TestUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Part
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

    override suspend fun getPostComments(id: GetCommentRequestModel): Response<CommentsResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.getPostComments(id)
            appData
        }

    override suspend fun deletePost(id: String): Response<JSONObject> =
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

    override suspend fun unBlockUser(post: UnblockRequestModel): Response<BlockResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val appData = communityApiService.unBlockUser(post)
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

    override suspend fun postComment(postId: RequestBody, text: RequestBody, tags: RequestBody,): Response<SendCommentResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.postComment(postId,text,tags)
        userData
    }

override suspend fun updateComment(postId: RequestBody, commentId: RequestBody, text: RequestBody, tags: RequestBody,): Response<SendCommentResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.updateComment(postId,commentId,text,tags)
        userData
    }

    override suspend fun postComment(
        postId: RequestBody,
        text: RequestBody,
        tags: RequestBody,
        postImage: MultipartBody.Part
    ): Response<SendCommentResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.postComment(postId,text,tags,postImage)
        userData
    }
    override suspend fun updateComment(
        postId: RequestBody,
        commentId: RequestBody,
        text: RequestBody,
        tags: RequestBody,
        postImage: MultipartBody.Part
    ): Response<SendCommentResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.updateComment(postId,commentId,text,tags,postImage)
        userData
    }

    override suspend fun createPost(
        description: RequestBody?,
        tags: RequestBody?,
        postImage1: MultipartBody.Part?,
        postImage2: MultipartBody.Part?,
        postImage3: MultipartBody.Part?
    ): Response<CreatePostResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.createPost(image_1 = postImage1,image_2 = postImage2,image_3 = postImage3,text = description, tags=tags)
        userData
    }

    override suspend fun updatePost(
        description: RequestBody?,
        tags: RequestBody?,
        removedImages: RequestBody?,
        postId: RequestBody?,
        postImage1: MultipartBody.Part?,
        postImage2: MultipartBody.Part?,
        postImage3: MultipartBody.Part?
    ): Response<CreatePostResponseModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.updatePost(image_1 = postImage1,image_2 = postImage2,image_3 = postImage3,text = description, tags=tags,removedImages=removedImages,postId)
        userData
    }

    override suspend fun updatePost(
        postId: RequestBody,
        tags: RequestBody?,
        area: RequestBody?,
        date: RequestBody?
    ): Response<CreatePostResponseModel> = withContext(
    Dispatchers.IO) {
        val userData = communityApiService.updatePost(postId=postId, tags=tags, farmArea = area, showingDate = date)
        userData
    }

    override suspend fun getBlockedUsersList(): Response<BlockedUsersListResponseModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = communityApiService.getBlockedUsersList()
        blockedUsers
    }


    override suspend fun deleteComment(postID:String, commentId:String): Response<DeleteCommentResponseModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = communityApiService.deleteComment(postID,commentId)
        blockedUsers
    }

    override suspend fun getFollowings(followRequestModel: FollowRequestModel): Response<FollowerResponseModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = communityApiService.getFollowings(followRequestModel)
        blockedUsers
    }

    override suspend fun getFollowers(followRequestModel: FollowRequestModel): Response<FollowerResponseModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = communityApiService.getFollowers(followRequestModel)
        blockedUsers
    }
}