package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.address.adapter.AddressDataAdapter
import agstack.gramophone.ui.address.adapter.CustomListAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import agstack.gramophone.ui.address.model.State
import agstack.gramophone.ui.language.model.languagelist.Language
import android.os.Bundle
import android.widget.ArrayAdapter
import java.util.ArrayList

interface AddressNavigator : BaseNavigator{
    fun updateDistrict(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateTehsil(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateVillage(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updatePinCode(adapter: CustomListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateUI(resultData: Bundle)
    fun goToApp()
    fun getState(): String?
    fun changeState()
    fun getAdapter(dataList: ArrayList<AddressDataModel>): CustomListAdapter
    fun closeDistrictDropDown()
    fun closeTehsilDropDown()
    fun closeVillageDropDown()
    fun closePincodeDropDown()

}