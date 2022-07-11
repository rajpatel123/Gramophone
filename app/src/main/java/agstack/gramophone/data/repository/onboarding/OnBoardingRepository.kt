package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import retrofit2.Response
import javax.inject.Singleton

//Rename this as OnBoarding Repository
@Singleton
interface OnBoardingRepository {
    suspend fun sendOTP(loginMap: SendOtpRequestModel): Response<GenerateOtpResponseModel>
}