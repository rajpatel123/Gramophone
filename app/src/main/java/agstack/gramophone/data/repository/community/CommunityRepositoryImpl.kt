package agstack.gramophone.data.repository.community

import agstack.gramophone.di.CommunityApiService
import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.postdetails.model.comments.CommentsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
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


}