package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.di.GramAppService
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
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnBoardingRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService
) : OnBoardingRepository {

    override suspend fun getInitialData(initiateAppDataRequestModel: InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel> = withContext(
        Dispatchers.IO) {
        val appData = gramAppService.getInitialData(initiateAppDataRequestModel)
        appData
    }

    override suspend fun sendOTP(sendOtpRequestModel:SendOtpRequestModel): Response<SendOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val sendOTP = gramAppService.sendOTP(sendOtpRequestModel)
        sendOTP
    }

    override suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val validateOtp = gramAppService.validateOTP(validateOtpRequestModel)
        validateOtp
    }

    override suspend fun resendOTP(sendOtpRequestModel: SendOtpRequestModel):Response<SendOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.resendOTP(sendOtpRequestModel)
        resendOtp
    }

    override suspend fun getLanguage(): Response<LanguageListResponse> = withContext(
        Dispatchers.IO) {
        val languageList = gramAppService.getLanguage()
        languageList
    }

    override suspend fun getCustomerLanguage(): Response<CustomerLanguageResponseModel> = withContext(
        Dispatchers.IO) {
        val languageList = gramAppService.getCustomerLanguage()
        languageList
    }


    override suspend fun updateLanguage(updateLanguageRequestModel: UpdateLanguageRequestModel):Response<UpdateLanguageResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.updateLanguage(updateLanguageRequestModel)
        resendOtp
    }


    override suspend fun getSecretKeys():Response<SecretKeysResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.getSecretKeys()
        resendOtp
    }

    override suspend fun updateLanguageWhileOnBoarding(updateLanguageRequestModel: UpdateLanguageRequestModel):Response<UpdateLanguageResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.updateLanguageWhileOnBoarding(updateLanguageRequestModel)
        resendOtp
    }

    override suspend fun getAddressDataByType(type:String, addressRequestModel: AddressRequestModel ):Response<StateResponseModel> = withContext(
        Dispatchers.IO){
        val resendOtp = gramAppService.getAddressData(type , addressRequestModel)
        resendOtp
    }

    override suspend fun getDistrict(type:String, addressRequestModel: AddressRequestModel ):Response<AddressResponseModel> = withContext(
        Dispatchers.IO){
        val resendOtp = gramAppService.getDistrict(type , addressRequestModel)
        resendOtp
    }


    override suspend fun updateAddress(updateAddressRequestModel: UpdateAddressRequestModel):Response<SendOtpResponseModel> = withContext(
        Dispatchers.IO){
        val addressUpdate = gramAppService.updateAddress(updateAddressRequestModel)
        addressUpdate
    }

    override suspend fun logoutUser():Response<LogoutResponseModel> = withContext(
        Dispatchers.IO){
        val logout = gramAppService.logoutUser()
        logout
    }


    override suspend fun getAddressByLatLong(addressRequestModel: AddressRequestWithLatLongModel):Response<StateResponseModel> = withContext(
        Dispatchers.IO){
        val addressData = gramAppService.getAddressByLatLong(addressRequestModel)
        addressData
    }

    override suspend fun getBanners(): Response<BannerResponse>  = withContext(
        Dispatchers.IO) {
        val bannerResponse = gramAppService.getBanners()
        bannerResponse
    }


    override suspend fun getLocationAddress(lat: String, key: String): Response<GoogleAddressResponseModel>  = withContext(
        Dispatchers.IO) {
        val bannerResponse = gramAppService.getLocationAddress(lat,key)
        bannerResponse
    }


    override suspend fun getProfile(): Response<ProfileResponse>  = withContext(
        Dispatchers.IO) {
        val profileResponse = gramAppService.getProfile()
        profileResponse
    }


    override suspend fun updateProfile(updateProfileModel: UpdateProfileModel): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO) {
        val profileResponse = gramAppService.updateProfile(updateProfileModel)
        profileResponse
    }

    override suspend fun sendOTPMobile(verifyOtpRequestModel:VerifyOTPRequestModel): Response<SendOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val sendOTP = gramAppService.sendOTPMobile(verifyOtpRequestModel)
        sendOTP
    }

    override suspend fun resendOTPMobile(resendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOTP = gramAppService.resendOTPMobile(resendOtpRequestModel)
        resendOTP
    }

    override suspend fun validateOtpMobile(validateOtpRequestModel: ValidateOtpMobileRequestModel): Response<SuccessStatusResponse> = withContext(
        Dispatchers.IO) {
        val validateOTPMobile = gramAppService.validateOTPMobile(validateOtpRequestModel)
        validateOTPMobile
    }


    override suspend fun getMentionTags(resendOtpRequestModel: MentionRequestModel): Response<MentionTagResponsemodel> = withContext(
        Dispatchers.IO) {
        val resendOTP = gramAppService.getMentionTags(resendOtpRequestModel)
        resendOTP
    }

    override suspend fun getHasTags(mentionRequestModel: MentionRequestModel): Response<HasgTagResponseModel> = withContext(
        Dispatchers.IO) {
        val validateOTPMobile = gramAppService.getHasTags(mentionRequestModel)
        validateOTPMobile
    }


    override suspend fun getProblemTags(mentionRequestModel: MentionRequestModel): Response<ProblemTagsResponseModel> = withContext(
        Dispatchers.IO) {
        val validateOTPMobile = gramAppService.getProblemTags(mentionRequestModel)
        validateOTPMobile


    }
    override suspend fun getMyGramophoneData(): Response<MyGramophoneResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramAppService.getMyGramophoneData()
            response
        }

    override suspend fun getFavouriteProduct(favouriteRequestModel: FavouriteRequestModel): Response<FeaturedProductResponseModel> =
        withContext(
            Dispatchers.IO
        ) {
            val response = gramAppService.getFavouriteProduct(favouriteRequestModel)
            response
        }


    override suspend fun getQuiz(): Response<QuizPollResponseModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getQuiz()
        blockedUsers
    }


    override suspend fun answerQuiz(answeredQuizPollRequestModel :AnsweredQuizPollRequestModel): Response<AnsweredQuizPollRequestModel> = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.answerQuiz(answeredQuizPollRequestModel)
        blockedUsers
    }

    override suspend fun getCropAdvisoryDetails(
        advisoryRequestModel: AdvisoryRequestModel,
        type:String
    ): Response<AdvisoryResponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getCropAdvisoryDetails(advisoryRequestModel,type)
        blockedUsers
    }

    override suspend fun getRecommendedProducts(recommendedProductRequestModel: RecommendedProductRequestModel): Response<RecommendedProductResponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getRecommendedProducts(recommendedProductRequestModel)
        blockedUsers
    }

    override suspend fun getCropProblems(cropProblemRequestModel: CropProblemRequestModel): Response<CropProblemResponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getCropProblems(cropProblemRequestModel)
        blockedUsers
    }

    override suspend fun getNotification(notificationRequestModel: NotificationRequestModel):Response<NotificationresponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getNotifications(notificationRequestModel)
        blockedUsers
    }


    override suspend fun saveToken(fcmRegistrationModel: FCMRegistrationModel):Response<NotificationresponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.saveToken(fcmRegistrationModel)
        blockedUsers
    }

    override suspend fun getRatingEligibilityData(productData: ProductData):Response<RatingEligibilityResponseModel>  = withContext(
        Dispatchers.IO) {
        val blockedUsers = gramAppService.getRatingEligibilityData(productData)
        blockedUsers
    }
}