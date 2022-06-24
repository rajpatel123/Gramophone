package agstack.gramophone.ui.profileselection.viewmodel

import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.profileselection.repository.ProfileSelectionRepository
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.hasInternetConnection
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
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
class ProfileSelectionViewModel @Inject constructor(
    private val repository: ProfileSelectionRepository,
    @ApplicationContext private val context: Context

) : BaseViewModel<ProfileSelectionNavigator>() {
    val updateProfileData: MutableLiveData<Resource<UpdateProfileTypeRes>> =
        MutableLiveData()

    fun updateProfileType(hashMap: HashMap<Any, Any>) = viewModelScope.launch {
        updateProfileCall(hashMap)
    }

    private suspend fun updateProfileCall(hashMap: HashMap<Any, Any>) {

        updateProfileData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = repository.updateProfileType(hashMap)
                updateProfileData.postValue(handleResponse(response))
            } else
                updateProfileData.postValue(Resource.Error("No Internet Connection"))
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> updateProfileData.postValue(Resource.Error("Network Failure"))
                else -> updateProfileData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleResponse(response: Response<UpdateProfileTypeRes>): Resource<UpdateProfileTypeRes> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}