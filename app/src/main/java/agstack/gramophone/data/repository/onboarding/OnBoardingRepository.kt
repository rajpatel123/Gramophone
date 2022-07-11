package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.RegisterDeviceRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import retrofit2.Response
import javax.inject.Singleton

//Rename this as OnBoarding Repository
@Singleton
interface OnBoardingRepository {
    suspend fun sendOTP(loginMap: SendOtpRequestModel): Response<SendOtpResponseModel>

    suspend fun getDeviceToken(initiateAppDataRequestModel: InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel>

    suspend fun validateOtp(validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel>
}