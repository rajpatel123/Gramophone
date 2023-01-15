package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.advisory.models.advisory.AdvisoryRequestModel
import agstack.gramophone.ui.advisory.models.advisory.AdvisoryResponseModel
import agstack.gramophone.ui.advisory.models.cropproblems.CropProblemRequestModel
import agstack.gramophone.ui.advisory.models.cropproblems.CropProblemResponseModel
import agstack.gramophone.ui.advisory.models.recomondedproducts.RecommendedProductRequestModel
import agstack.gramophone.ui.advisory.models.recomondedproducts.RecommendedProductResponseModel
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.createnewpost.model.MentionRequestModel
import agstack.gramophone.ui.createnewpost.model.MentionTagResponsemodel
import agstack.gramophone.ui.createnewpost.model.problems.ProblemTagsResponseModel
import agstack.gramophone.ui.createnewpost.view.model.hashtags.HasgTagResponseModel
import agstack.gramophone.ui.favourite.model.FavouriteRequestModel
import agstack.gramophone.ui.favourite.model.FeaturedProductResponseModel
import agstack.gramophone.ui.home.featured.ratingeligibility.RatingEligibilityResponseModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.AnsweredQuizPollRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.QuizPollResponseModel
import agstack.gramophone.ui.home.view.fragments.gramophone.model.MyGramophoneResponseModel
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.model.FCMRegistrationModel
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.SecretKeysResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.notification.model.NotificationRequestModel
import agstack.gramophone.ui.notification.model.NotificationresponseModel
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.profile.model.ValidateOtpMobileRequestModel
import agstack.gramophone.ui.splash.model.language.CustomerLanguageResponseModel
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.ui.userprofile.verifyotp.model.VerifyOTPRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Singleton

@Singleton
interface OnBoardingRepository {

    suspend fun getLanguage(): Response<LanguageListResponse>

    suspend fun getCustomerLanguage(): Response<CustomerLanguageResponseModel>


    suspend fun updateLanguage(updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>

    suspend fun getSecretKeys(): Response<SecretKeysResponseModel>

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

    suspend fun getProblemTags(mentionRequestModel: MentionRequestModel): Response<ProblemTagsResponseModel>

    suspend fun getMyGramophoneData(): Response<MyGramophoneResponseModel>

    suspend fun getFavouriteProduct(favouriteRequestModel: FavouriteRequestModel): Response<FeaturedProductResponseModel>

    suspend fun getQuiz(): Response<QuizPollResponseModel>

    suspend fun answerQuiz(answeredQuizPollRequestModel: AnsweredQuizPollRequestModel): Response<AnsweredQuizPollRequestModel>

    suspend fun getCropAdvisoryDetails(advisoryRequestModel: AdvisoryRequestModel, type: String): Response<AdvisoryResponseModel>

    suspend fun getRecommendedProducts(body: RecommendedProductRequestModel): Response<RecommendedProductResponseModel>

    suspend fun getCropProblems(cropProblemRequestModel: CropProblemRequestModel): Response<CropProblemResponseModel>

    suspend fun getNotification(notificationRequestModel: NotificationRequestModel): Response<NotificationresponseModel>

    suspend fun saveToken(fcmRegistrationModel: FCMRegistrationModel): Response<NotificationresponseModel>

    suspend fun getRatingEligibilityData(productData: ProductData): Response<RatingEligibilityResponseModel>


}