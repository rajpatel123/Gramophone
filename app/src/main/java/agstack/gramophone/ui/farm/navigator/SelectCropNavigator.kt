package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.CropData

interface SelectCropNavigator : BaseNavigator {
    fun setToolbarTitle(title:String)

    fun updateCropAdapter(cropList: List<CropData>)

    fun onSearchViewClearClick()

    fun notifyAdapter(position : Int)

}