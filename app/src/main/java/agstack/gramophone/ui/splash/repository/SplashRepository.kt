package agstack.gramophone.ui.splash.repository

import agstack.gramophone.retrofit.ApiHelper


class SplashRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}