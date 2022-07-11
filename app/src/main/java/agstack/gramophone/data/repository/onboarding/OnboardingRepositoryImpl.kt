package agstack.gramophone.data.repository.onboarding

import agstack.gramophone.di.GramoAppService
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingRepositoryImpl @Inject constructor(
    private val gramoAppService: GramoAppService
) : OnboardingRepository {

    override suspend fun sendOTP(loginMap: HashMap<Any, Any>): Response<GenerateOtpResponseModel> = withContext(
        Dispatchers.IO) {
        val popular = gramoAppService.sendOTP(loginMap)
        popular
    }

}