package agstack.gramophone.ui.language.repository

import agstack.gramophone.retrofit.ApiHelper


class LanguageRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.loginUser()
}