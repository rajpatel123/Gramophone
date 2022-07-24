package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.StateNavigator
import agstack.gramophone.ui.address.adapter.StateListAdapter
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StateSelectionViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : BaseViewModel<StateNavigator>() {

    private lateinit var stateListAdapter: StateListAdapter
    private var stateListData: StateResponseModel? = null
    var state: State? = null
    var isStateSelected: Boolean? = false
    var stateName: String? = ""
    fun fetchStateList() = viewModelScope.launch {
        getStateList()
    }

    fun onStateUpdate(v: View) {
        if (state != null) {
            getNavigator()?.moveToNext(Bundle().apply {
                putString(Constants.STATE, state?.name)
            })
        } else {
            getNavigator()?.onError(getNavigator()?.getMessage(R.string.select_state_error))
        }


    }

    private suspend fun getStateList() {
        getNavigator()?.onLoading()
        try {
            if (getNavigator()?.isNetworkAvailable() == true) {
                val response = onBoardingRepository.getAddressDataByType(
                    "state",
                    AddressRequestModel("", "", "", "", "", "")
                )

                stateListData = handleGetStateListResponse(response).data!!

                if (Constants.GP_API_STATUS.equals(stateListData?.gp_api_status)) {
                    stateListAdapter = StateListAdapter(stateListData?.gp_api_response_data?.state_top_list!!)
                    getNavigator()?.updateStateList(stateListAdapter) {
                        state = it
                        getNavigator()?.onStateSelected(state?.name)
                    }
                }else{
                    getNavigator()?.onError(stateListData?.gp_api_message)

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

    private fun handleGetStateListResponse(response: Response<StateResponseModel>): ApiResponse<StateResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return ApiResponse.Success(resultResponse)
            }
        }
        return ApiResponse.Error(response.message())
    }

    fun onRemoveStateSelectionClicked() {
        getNavigator()?.onRemoveSelection()
    }

    fun onOthersClicked() {
        if (stateListData != null) {
            val bundle = Bundle()
            bundle.putParcelable(Constants.STATE_LIST, stateListData?.gp_api_response_data)
            getNavigator()?.selectOtherState(bundle)
        }

    }

    fun setStateSelection(stateName: String?) {
        this.stateName = stateName
        getNavigator()?.onStateSelected(stateName)
    }

    fun resetStateSelection() {
        stateListAdapter.mStateList.forEach {
            it.selected=false
        }
        state=null
        stateListAdapter.notifyDataSetChanged()
    }

}