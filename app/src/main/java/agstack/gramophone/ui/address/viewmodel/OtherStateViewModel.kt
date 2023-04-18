package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.OtherStateNavigator
import agstack.gramophone.ui.address.adapter.OtherStateListAdapter
import agstack.gramophone.ui.address.adapter.StateListAdapter
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.GpApiResponseData
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Utility
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OtherStateViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<OtherStateNavigator>() {
    var state: State? = null
    fun loadList(stateList: GpApiResponseData) {
        getNavigator()?.updateStateList(OtherStateListAdapter(stateList.state_list!!)) {
            state = it
            getNavigator()?.onStateSelected(state?.name)
        }
    }

    fun onCloseClicked() {
        getNavigator()?.closeStateList()
    }

     fun getStateList() {
        viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    val response = onBoardingRepository.getAddressDataByType(
                        "state",
                        AddressRequestModel("", "", "", "", "", "")
                    )
                    if (response.isSuccessful && Constants.GP_API_STATUS.equals(response.body()?.gp_api_status)) {
                        getNavigator()?.updateStateList(OtherStateListAdapter(response.body()?.gp_api_response_data?.state_list!!)) {
                            state = it
                            getNavigator()?.onStateSelected(state?.name)
                        }
                    } else {
                        getNavigator()?.onError(Utility.getErrorMessage(
                            response.errorBody()
                        ))

                    }

                } else
                    getNavigator()?.onError(getNavigator()?.getMessage(R.string.no_internet)!!)
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.network_failure)!!)
                    //else -> getNavigator()?.onError(getNavigator()?.getMessage(R.string.some_thing_went_wrong)!!)
                }
            }
        }
    }

}