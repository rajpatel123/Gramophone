package agstack.gramophone.ui.gramcash.expiringsoon

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.referandearn.transaction.GramCashTransactionListAdapter

interface GCExpiringSoonNavigator :BaseNavigator {
     fun setExpireTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter)
    fun showLoaderFooter()
    fun onListUpdated()
    fun goBack()
}