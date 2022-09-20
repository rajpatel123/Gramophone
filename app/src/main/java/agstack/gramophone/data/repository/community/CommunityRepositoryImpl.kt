package agstack.gramophone.data.repository.community

import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.di.CommunityApiService
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.userprofile.model.TestUserModel
import agstack.gramophone.ui.userprofile.model.UserModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityRepositoryImpl @Inject constructor(
    private val communityApiService: CommunityApiService
) : CommunityRepository {

    override suspend fun getCommunityPost(): Response<String> = withContext(
        Dispatchers.IO) {
        val appData = communityApiService.getCommunityPost()
        appData
    }


    override suspend fun getCommunityDilip(): Response<String> = withContext(
        Dispatchers.IO) {
        val appData = communityApiService.getCommunityPost()
        appData
    }


    override suspend fun updateUserProfileImage(@Part postImage: MultipartBody.Part): Response<TestUserModel> = withContext(
        Dispatchers.IO) {
        val userData = communityApiService.updateUserProfileImage(postImage)
        userData
    }


}