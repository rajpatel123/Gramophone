package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.ui.farm.model.Data

class ViewAllFarmsAdapter(
    farmItemList: List<List<Data>>?,
    headerListener: (List<Data>) -> Unit,
    contentListener: (List<Data>) -> Unit,
    footerListener: (List<Data>) -> Unit,
) : FarmAdapter(farmItemList, headerListener, contentListener, footerListener)