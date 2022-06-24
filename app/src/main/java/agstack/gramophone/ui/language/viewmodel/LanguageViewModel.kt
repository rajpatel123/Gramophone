package agstack.gramophone.ui.language.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.model.RegisterDeviceModel
import agstack.gramophone.ui.language.model.RegistrerDeviceRquestModel
import agstack.gramophone.ui.language.repository.LanguageRepository
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.hasInternetConnection
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
   private val languageRepository: LanguageRepository,
   @ApplicationContext private val context: Context

) : BaseViewModel<LanguageActivityNavigator>() {

   val registerDeviceModel: MutableLiveData<Resource<RegisterDeviceModel>> =
      MutableLiveData()

   fun getDeviceToken(loginMap: RegistrerDeviceRquestModel) = viewModelScope.launch {
      getToken(loginMap)
   }

   private suspend fun getToken(loginMap: RegistrerDeviceRquestModel) {

      registerDeviceModel.postValue(Resource.Loading())
      try {
         if (hasInternetConnection(context)) {
            val response = languageRepository.getDeviceToken(loginMap)
            val registerDeviceModel = handleOrderResponse(response).data
            SharedPreferencesHelper.instance?.putString(
               SharedPreferencesKeys.session_token,
               registerDeviceModel?.data?.session_token
            )
            SharedPreferencesHelper.instance?.putString(
               SharedPreferencesKeys.HashedAndroidID,
               registerDeviceModel?.data?.android_id_hashed
            )
         } else
            registerDeviceModel.postValue(Resource.Error("No Internet Connection"))
      } catch (ex: Exception) {
         when (ex) {
            is IOException -> registerDeviceModel.postValue(Resource.Error("Network Failure"))
            else -> registerDeviceModel.postValue(Resource.Error("Conversion Error"))
         }
      }
   }

   private fun handleOrderResponse(response: Response<RegisterDeviceModel>): Resource<RegisterDeviceModel> {
      if (response.isSuccessful) {
         response.body()?.let { resultResponse ->
            return Resource.Success(resultResponse)
         }
      }
      return Resource.Error(response.message())
   }
}