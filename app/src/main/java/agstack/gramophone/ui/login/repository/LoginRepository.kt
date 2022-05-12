package agstack.gramophone.ui.login.repository

import agstack.gramophone.retrofit.ApiHelper


class LoginRepository(private val apiHelper: ApiHelper) {

    suspend fun loginUser() = apiHelper.loginUser()
}