package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.address.adapter.OtherStateListAdapter
import agstack.gramophone.ui.address.model.State

interface OtherStateNavigator: BaseNavigator {
    fun updateStateList(otherStateListAdapter: OtherStateListAdapter, onStateSelected: (State) -> Unit)
    fun onStateSelected(name: String?)
    fun closeStateList()

}