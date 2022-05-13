package agstack.gramophone.ui.apptour.repository

import agstack.gramophone.retrofit.ApiHelper


class AppTourRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}