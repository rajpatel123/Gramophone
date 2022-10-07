package agstack.gramophone.ui.offerslist

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter
import agstack.gramophone.ui.offerslist.model.DataItem

interface OffersListNavigator :BaseNavigator {
     fun setOffersListAdapter(offersListAdapter: OffersListAdapter,onOfferSelected: (DataItem) -> Unit)
     fun getLanguageCode(): String?
    fun showLoaderFooter()
    fun onListUpdated()
    fun ShowNoListView(showNoItemView: Boolean)
}