package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator

interface AddFarmNavigator : BaseNavigator {
    fun onFarmAdded()
    fun setFarmUnitsAdapter(units: List<String>)
    fun showDatePicker()
    fun getDate(): String
    fun getArea(): Double
    fun getAreaUnit(): String
}
