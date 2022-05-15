package agstack.gramophone.ui.home.repository

import agstack.gramophone.retrofit.ApiHelper


class HomeRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}