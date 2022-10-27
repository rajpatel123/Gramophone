package agstack.gramophone.data.repository.settings

import agstack.gramophone.ui.referandearn.model.GramCashResponseModel
import agstack.gramophone.ui.referandearn.transaction.TransactionRequestModel
import agstack.gramophone.ui.referandearn.transaction.model.GramCashTxnResponseModel
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.utils.FileUploadRequestBody
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface SettingsRepository {
    suspend fun optInOutForWhatsappUpdates(s: String): Response<WhatsAppOptInResponseModel>
    suspend fun unBlockUser(customerId: Int): Response<BlockedUsersListResponseModel>

    suspend fun getGramCash():Response<GramCashResponseModel>
    suspend fun getGramCashTxn(type:String,requestBody: TransactionRequestModel):Response<GramCashTxnResponseModel>


}