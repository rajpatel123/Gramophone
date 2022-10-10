package agstack.gramophone.ui.farm.navigator

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.home.adapter.ShopByCropsAdapter

interface SelectCropNavigator : BaseNavigator {
    fun setToolbarTitle(title:String)

    fun setSelectCropAdapter(selectCropAdapter: SelectCropAdapter)

    fun notifyAdapter(position : Int)

}