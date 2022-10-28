package agstack.gramophone.data.repository.settings

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.referandearn.model.GramCashResponseModel
import agstack.gramophone.ui.referandearn.transaction.TransactionRequestModel
import agstack.gramophone.ui.referandearn.transaction.model.GramCashTxnResponseModel
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



    override suspend fun unBlockUser(customerId: Int): Response<BlockedUsersListResponseModel> = withContext(
    Dispatchers.IO) {
        val blockedUsers = gramAppService.unBlockUser(customerId)
        blockedUsers
    }

    override suspend fun getGramCash(): Response<GramCashResponseModel> = withContext(
        Dispatchers.IO) {
       val gramCashResponse = gramAppService.getGramCash()
        gramCashResponse
    }


    override suspend fun getGramCashTxn(type: String,requestModel:TransactionRequestModel): Response<GramCashTxnResponseModel>  = withContext(
        Dispatchers.IO) {
        val gramCashResponse = gramAppService.getGramCashTxn(type,requestModel)
        gramCashResponse
    }


}


