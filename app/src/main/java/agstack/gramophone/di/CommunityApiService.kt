package agstack.gramophone.di

import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedusersResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.LikedUsersRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunityApiService {

    @POST("/api/v2/posts/all")
    suspend fun getCommunityPost(@Body sort: CommunityRequestModel?): Response<CommunityHomeResponseModel>


    @POST("/api/v2/likes/get-likes")
    suspend fun getLikedUsers(@Body likedUsersRequestModel: LikedUsersRequestModel?): Response<LikedusersResponseModel>
}