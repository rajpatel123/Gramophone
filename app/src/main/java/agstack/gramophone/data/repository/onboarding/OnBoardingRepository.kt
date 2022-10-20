package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.createnewpost.model.MentionRequestModel
import agstack.gramophone.ui.createnewpost.model.MentionTagResponsemodel
import agstack.gramophone.ui.createnewpost.view.model.hashtags.HasgTagResponseModel
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.profile.model.ValidateOtpMobileRequestModel
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.ui.userprofile.verifyotp.model.VerifyOTPRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface OnBoardingRepository {
    suspend fun getLanguage(): Response<LanguageListResponse>


    suspend fun updateLanguage(updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>

    suspend fun updateLanguageWhileOnBoarding(updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>

    suspend fun sendOTP(sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>

    suspend fun getInitialData(initiateAppDataRequestModel: InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel>

    suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel>

    suspend fun resendOTP(sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>

    suspend fun getAddressDataByType(type : String, sendOtpRequestModel: AddressRequestModel): Response<StateResponseModel>

    suspend fun getDistrict(type : String, sendOtpRequestModel: AddressRequestModel): Response<AddressResponseModel>

    suspend fun updateAddress(updateAddressRequestModel: UpdateAddressRequestModel): Response<SendOtpResponseModel>

    suspend fun logoutUser(): Response<LogoutResponseModel>

    suspend fun getAddressByLatLong(addressRequestModel: AddressRequestWithLatLongModel): Response<StateResponseModel>

    suspend fun getBanners(): Response<BannerResponse>

    suspend fun getProfile(): Response<ProfileResponse>

    suspend fun getLocationAddress(lat: String, key: String): Response<GoogleAddressResponseModel>

    suspend fun updateProfile(updateProfileModel: UpdateProfileModel):Response<SuccessStatusResponse>

    suspend fun sendOTPMobile(sendOtpRequestModel: VerifyOTPRequestModel): Response<SendOtpResponseModel>

    suspend fun resendOTPMobile(resendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>

    suspend fun validateOtpMobile(validateOtpRequestModel: ValidateOtpMobileRequestModel): Response<SuccessStatusResponse>

    suspend fun getMentionTags(mentionRequestModel: MentionRequestModel): Response<MentionTagResponsemodel>


    suspend fun getHasTags(mentionRequestModel: MentionRequestModel): Response<HasgTagResponseModel>

}