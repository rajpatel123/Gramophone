package agstack.gramophone.ui.offerslist

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter

interface OffersListNavigator :BaseNavigator {
     fun setOffersListAdapter(offersListAdapter: OffersListAdapter)
}