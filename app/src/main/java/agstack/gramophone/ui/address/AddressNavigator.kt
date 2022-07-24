package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.address.adapter.AddressDataAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.language.model.languagelist.Language
import android.os.Bundle
import android.widget.ArrayAdapter
import java.util.ArrayList

interface AddressNavigator : BaseNavigator{
    fun updateDistrict(adapter: AddressDataAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateTehsil(function1: ArrayList<String>, function: () -> Unit)
    fun updateVillage(function1: ArrayList<String>, function: () -> Unit)
    fun updatePinCode(function1: ArrayList<String>, function: () -> Unit)
    fun updateUI(resultData: Bundle)
    fun goToApp()
    fun getState(): String?
    fun changeState()
    fun getAdapter(dataList: ArrayList<AddressDataModel>): AddressDataAdapter

}