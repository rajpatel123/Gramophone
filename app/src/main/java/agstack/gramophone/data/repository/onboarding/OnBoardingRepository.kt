package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface OnBoardingRepository {
    suspend fun getLanguage(): Response<LanguageListResponse>

    suspend fun sendOTP(sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>

    suspend fun getInitialData(initiateAppDataRequestModel: InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel>

    suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel>


    suspend fun resendOTP(sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>
}