package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData
import java.util.*

interface AddFarmNavigator : BaseNavigator {
    fun onFarmAdded()
    fun setFarmUnitsAdapter(units: List<GpApiResponseData>)
    fun showDatePicker()
    fun getDate(): String
    fun getArea(): Double
    fun getAreaUnit(): GpApiResponseData?
    fun sendSaveFarmMoEngageEvents(farmId: Int?, crop: String, sowingDate: String, area: String)
    fun getDateForEvent(): Date
}
