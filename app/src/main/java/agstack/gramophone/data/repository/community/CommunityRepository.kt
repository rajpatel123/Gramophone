package agstack.gramophone.data.repository.community

import agstack.gramophone.ui.userprofile.model.TestUserModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Singleton

@Singleton
interface CommunityRepository {
    suspend fun getCommunityPost(): Response<String>
    suspend fun getCommunityDilip(): Response<String>
    suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part): Response<TestUserModel>
}