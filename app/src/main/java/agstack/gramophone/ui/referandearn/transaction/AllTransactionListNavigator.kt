package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.base.BaseNavigator

interface AllTransactionListNavigator: BaseNavigator {
     fun setAllTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter)
     fun setReferralTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter)
    fun showLoaderFooter()
    fun onListUpdated()
    fun showLoaderFooterReferral()
    fun onListUpdatedReferral()
}