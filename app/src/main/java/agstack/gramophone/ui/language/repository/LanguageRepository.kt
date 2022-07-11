package agstack.gramophone.ui.language.repository

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegisterDeviceRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepository @Inject constructor(
    private val gramAppService: GramAppService
) {

    suspend fun getDeviceToken(loginMap: RegisterDeviceRequestModel): Response<RegisterDeviceModel> = withContext(
        Dispatchers.IO) {
        val popular = gramAppService.getDeviceToken(loginMap)
        popular
    }

}