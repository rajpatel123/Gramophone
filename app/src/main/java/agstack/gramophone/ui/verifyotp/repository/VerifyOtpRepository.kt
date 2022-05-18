package agstack.gramophone.ui.verifyotp.repository

import agstack.gramophone.retrofit.ApiHelper


class VerifyOtpRepository(private val apiHelper: ApiHelper) {

    suspend fun loginUser() = apiHelper.loginUser()
}