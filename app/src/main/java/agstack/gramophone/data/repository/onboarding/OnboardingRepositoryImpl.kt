package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
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


    override suspend fun updateLanguage(updateLanguageRequestModel: UpdateLanguageRequestModel):Response<UpdateLanguageResponseModel> = withContext(
        Dispatchers.IO) {
        val resendOtp = gramAppService.updateLanguage(updateLanguageRequestModel)
        resendOtp
    }




}