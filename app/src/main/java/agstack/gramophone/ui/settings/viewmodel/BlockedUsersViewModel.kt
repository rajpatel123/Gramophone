package agstack.gramophone.ui.settings.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.community.CommunityRepository
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.settings.BlockedUsersAdapter
import agstack.gramophone.ui.settings.BlockedUsersNavigator
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.UnblockRequestModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Utility
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BlockedUsersViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val communityRepository: CommunityRepository,
) : BaseViewModel<BlockedUsersNavigator>() {
    fun getBlockedUsersList() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val blockedUsersList = communityRepository.getBlockedUsersList()

                    val optInResponseData = handleResponse(blockedUsersList).data

                    if (optInResponseData?.data != null) {
                        getNavigator()?.updateUserList(BlockedUsersAdapter(optInResponseData?.data!!)) {
                            unblockUser(it._id)
                        }
                    } else {
                        getNavigator()?.updateUserList(null) {
                            unblockUser(it._id)
                        }
                        getNavigator()?.onError(Utility.getErrorMessage(blockedUsersList.errorBody()))
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }

        }

    }

    private fun unblockUser(customerId: String) {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val blockedUserResponse = communityRepository.unBlockUser(
                        UnblockRequestModel("unblock", customerId))

                     getNavigator()?.sendUnBlockUserMoEngageEvent(customerId)

                    if (blockedUserResponse.isSuccessful) {
                        getBlockedUsersList()
                    } else {
                        getNavigator()?.onError(Utility.getErrorMessage(blockedUserResponse.errorBody()))
                    }
                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
//                    else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
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