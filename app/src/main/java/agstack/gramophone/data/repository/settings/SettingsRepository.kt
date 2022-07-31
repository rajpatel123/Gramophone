package agstack.gramophone.data.repository.settings

import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.viewmodel.LoginViewModel
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface SettingsRepository {
    suspend fun optInOutForWhatsappUpdates(s: String): Response<WhatsAppOptInResponseModel>

}