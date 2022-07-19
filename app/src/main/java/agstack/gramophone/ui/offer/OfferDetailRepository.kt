package agstack.gramophone.ui.offer

import agstack.gramophone.di.GramAppService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfferDetailRepository @Inject constructor(
    private val gramAppService: GramAppService
) {

}