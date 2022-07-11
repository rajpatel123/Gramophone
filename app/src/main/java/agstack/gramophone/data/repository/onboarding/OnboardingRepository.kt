package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import retrofit2.Response
import javax.inject.Singleton


@Singleton
interface OnboardingRepository {

    suspend fun sendOTP(loginMap: HashMap<Any, Any>): Response<GenerateOtpResponseModel>
}