package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.settings.BlockedUsersAdapter
import agstack.gramophone.ui.settings.BlockedUsersNavigator
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<BlockedUsersNavigator>() {
    fun getBlockedUsersList() {
        viewModelScope.async {
            if (getNavigator()?.isNetworkAvailable() == true) {
                try {
                    if (getNavigator()?.isNetworkAvailable() == true) {
                        val blockedUsersDeferred = async {
                            settingsRepository.getBlockedUsersList()
                        }
                        val blockedUsersList = blockedUsersDeferred.await()

                        val optInResponseData = handleResponse(blockedUsersList).data

                        if (Constants.GP_API_STATUS.equals(optInResponseData?.gp_api_status)) {
                            getNavigator()?.updateUserList(BlockedUsersAdapter(optInResponseData?.gp_api_response_data?.block_list!!)){
                            }
                        } else {
                            getNavigator()?.onError(optInResponseData?.gp_api_message)
                        }
                    } else
                        getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                        else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                    }
                }
            }
        }

    }


    private fun handleResponse(response: Response<BlockedUsersListResponseModel>): ApiResponse<BlockedUsersListResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

}