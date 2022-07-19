package agstack.gramophone.ui.address.viewmodel

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.onboarding.OnBoardingRepository
import agstack.gramophone.ui.address.OtherStateNavigator
import agstack.gramophone.ui.address.adapter.OtherStateListAdapter
import agstack.gramophone.ui.address.model.GpApiResponseData
import agstack.gramophone.ui.address.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
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
}