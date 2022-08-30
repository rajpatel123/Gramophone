package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
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


    override suspend fun updateLanguage(updateLanguageRequestModel: UpdateLanguageRequestModel):Response<UpdateLanguageResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.updateLanguage(updateLanguageRequestModel)
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


    override suspend fun updateAddressByLatLong(addressRequestModel: AddressRequestWithLatLongModel):Response<AddressDataByLatLongResponseModel> = withContext(
        Dispatchers.IO){
        val addressData = gramAppService.updateAddressByLatLong(addressRequestModel)
        addressData
    }

    override suspend fun getBanners(): Response<BannerResponse>  = withContext(
        Dispatchers.IO) {
        val bannerResponse = gramAppService.getBanners()
        bannerResponse
    }


    override suspend fun getLocationAddress(lat: String, key: String): Response<JSONObject>  = withContext(
        Dispatchers.IO) {
        val bannerResponse = gramAppService.getLocationAddress(lat,key)
        bannerResponse
    }

}