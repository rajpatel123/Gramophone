package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.address.adapter.OtherStateListAdapter
import agstack.gramophone.ui.address.adapter.StateListAdapter
import agstack.gramophone.ui.address.model.State
import android.os.Bundle

interface StateNavigator: BaseNavigator {
    fun updateStateList(stateListAdapter: StateListAdapter, onStateSelected: (State) -> Unit)
    fun onStateSelected()
    fun selectOtherState(bundle: Bundle)
    fun onRemoveSelection()
    fun getBundle(): Bundle?
    fun fetchStateList()
    fun checkPermission(): Boolean
    fun requestForLocation()

}