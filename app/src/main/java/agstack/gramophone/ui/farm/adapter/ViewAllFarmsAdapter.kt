package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.ui.farm.model.Data

class ViewAllFarmsAdapter(
    farmItemList: List<List<Data>>?,
    listener: (List<Data>) -> Unit,
    listener2: (List<Data>) -> Unit
) : FarmAdapter(farmItemList, listener, listener2)