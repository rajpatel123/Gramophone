package agstack.gramophone.ui.verifyotp.repository

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.login.model.GenerateOtpResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerifyOtpRepository @Inject constructor(
    private val gramAppService: GramAppService
) {


}
