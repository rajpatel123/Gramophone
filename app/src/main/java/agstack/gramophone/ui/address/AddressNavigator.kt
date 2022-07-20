package agstack.gramophone.ui.address

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.address.model.State
import android.os.Bundle
import java.util.ArrayList

interface AddressNavigator : BaseNavigator{
    fun updateDistrict(function1: ArrayList<String>, function: () -> Unit)
    fun updateTehsil(function1: ArrayList<String>, function: () -> Unit)
    fun updateVillage(function1: ArrayList<String>, function: () -> Unit)
    fun updatePinCode(function1: ArrayList<String>, function: () -> Unit)
    fun updateUI(resultData: Bundle)
    fun goToApp()
    fun getState(): String?
    fun changeState()

}