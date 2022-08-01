package agstack.gramophone.data.repository.settings

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val gramAppService: GramAppService
) :SettingsRepository {
    override suspend fun optInOutForWhatsappUpdates(type: String): Response<WhatsAppOptInResponseModel> = withContext(
    Dispatchers.IO) {
        val optInOut = gramAppService.optInOutForWhatsappUpdates(type)
        optInOut
    }

    override suspend fun getBlockedUsersList(): Response<BlockedUsersListResponseModel> = withContext(
    Dispatchers.IO) {
        val blockedUsers = gramAppService.getBlockedUsersList()
        blockedUsers
    }



}


