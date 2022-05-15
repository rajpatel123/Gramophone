package agstack.gramophone.retrofit

class ApiHelper(private val apiService: ApiService) {

    suspend fun loginUser() = apiService.loginUser()


}