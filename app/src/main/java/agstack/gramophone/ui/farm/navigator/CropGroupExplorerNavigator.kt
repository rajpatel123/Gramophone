package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData

interface CropGroupExplorerNavigator  : BaseNavigator {
    fun setToolbarTitle(title: String)
    fun setAdapter(cropList: List<Data>)
    fun onAddHarvestQues()
    fun setFarmUnits(units: List<GpApiResponseData>)
    fun refreshFarm(data: Data)
}