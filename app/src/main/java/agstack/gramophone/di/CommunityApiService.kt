package agstack.gramophone.di

import agstack.gramophone.ui.userprofile.model.TestUserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface CommunityApiService {

    @GET("/api/v2/posts/1.2/all")
    suspend fun getCommunityPost(): Response<String>



    @Multipart
    @PUT("api/v2/profiles")
    suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part):Response<TestUserModel>


}