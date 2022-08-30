package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.di.GPSTracker
import agstack.gramophone.ui.address.adapter.AddressDataListAdapter
import agstack.gramophone.ui.address.model.AddressDataModel
import android.os.Bundle
import java.util.ArrayList

interface AddressNavigator : BaseNavigator{
    fun updateDistrict(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateTehsil(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updateVillage(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun updatePinCode(adapter: AddressDataListAdapter, onSelect: (AddressDataModel) -> Unit)
    fun goToApp()
    fun getState(): String?
    fun getAdapter(dataList: ArrayList<AddressDataModel>): AddressDataListAdapter
    fun closeDistrictDropDown()
    fun closeTehsilDropDown()
    fun closeVillageDropDown()
    fun closePincodeDropDown()
    fun getGPSTracker(): GPSTracker
    fun onBackPressClick()


}