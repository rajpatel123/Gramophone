package agstack.gramophone.ui.profile.repository

import agstack.gramophone.retrofit.ApiHelper


class ProfileSelectionRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.loginUser()
}