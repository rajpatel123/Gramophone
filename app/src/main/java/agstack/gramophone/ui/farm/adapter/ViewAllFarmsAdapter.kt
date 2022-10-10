package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.ui.farm.model.Data

class ViewAllFarmsAdapter(
    farmItemList: List<List<Data>>?,
    listener: (Data) -> Unit,
    listener2: (Data) -> Unit
) : FarmAdapter(farmItemList, listener, listener2) {
}