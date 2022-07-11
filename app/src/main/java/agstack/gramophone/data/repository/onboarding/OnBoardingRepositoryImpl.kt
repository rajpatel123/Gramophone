package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class OnBoardingRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService
) : OnBoardingRepository {
    override suspend fun sendOTP(sendOtpRequestModel:SendOtpRequestModel): Response<GenerateOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val popular = gramAppService.sendOTP(sendOtpRequestModel)
        popular
    }


}