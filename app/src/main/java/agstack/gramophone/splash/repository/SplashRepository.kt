package agstack.gramophone.splash.repository

import agstack.gramophone.retrofit.ApiHelper


class SplashRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}