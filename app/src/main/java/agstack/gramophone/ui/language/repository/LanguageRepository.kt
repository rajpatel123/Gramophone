package agstack.gramophone.ui.language.repository

import agstack.gramophone.di.GramoAppService
import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegistrerDeviceRquestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepository @Inject constructor(
    private val gramoAppService: GramoAppService
) {

    suspend fun getDeviceToken(loginMap: RegistrerDeviceRquestModel): Response<RegisterDeviceModel> = withContext(
        Dispatchers.IO) {
        val popular = gramoAppService.getDeviceToken(loginMap)
        popular
    }

}