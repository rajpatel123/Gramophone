package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.farm.adapter.ViewAllFarmsAdapter
import agstack.gramophone.ui.farm.model.AddFarmResponse
import agstack.gramophone.ui.farm.model.CustomerFarm
import agstack.gramophone.ui.farm.model.Data
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.farm.model.unit.GpApiResponseData

interface ViewAllFarmsNavigator : BaseNavigator {
    fun setToolbarTitle(title: String)
    fun setViewAllFarmsAdapter(farmsList: List<List<Data>>, isCustomerFarms: Boolean, isOldFarms: Boolean)
    fun setFarmUnits(units: List<GpApiResponseData>)
    fun onAddHarvestQues()
}