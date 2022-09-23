package agstack.gramophone.ui.profileselection.repository

import agstack.gramophone.di.GramAppService
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileSelectionRepository @Inject constructor(
    private val gramAppService: GramAppService
) {

    suspend fun updateProfileType(profileTypeMap: HashMap<Any, Any>): Response<UpdateProfileTypeRes> =
        withContext(
            Dispatchers.IO
        ) {
            gramAppService.updateProfileType(profileTypeMap)
        }

}