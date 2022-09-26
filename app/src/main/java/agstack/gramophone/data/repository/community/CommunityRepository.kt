package agstack.gramophone.data.repository.community

import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.BookmarkPostResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikePostResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.likes.PostRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.LikedUsersRequestModel
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.postdetails.model.PostDetailResponseModel
import agstack.gramophone.ui.postdetails.model.comments.CommentsResponseModel
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface CommunityRepository {
    suspend fun getCommunityPost(sort: CommunityRequestModel): Response<CommunityHomeResponseModel>

    suspend fun getLikedUsers(sort: PostRequestModel): Response<LikedusersResponseModel>

    suspend fun likePost(post: PostRequestModel): Response<LikePostResponseModel>

    suspend fun bookmarkPost(post: PostRequestModel): Response<BookmarkPostResponse>

    suspend fun pinPost(post: PostRequestModel): Response<BookmarkPostResponse>

    suspend fun getPostDetails(post: String): Response<PostDetailResponseModel>

    suspend fun getPostComments(post: PostRequestModel): Response<CommentsResponseModel>

    suspend fun deletePost(post: PostRequestModel): Response<CommentsResponseModel>

    suspend fun deletePostComment(post: PostRequestModel): Response<CommentsResponseModel>
}