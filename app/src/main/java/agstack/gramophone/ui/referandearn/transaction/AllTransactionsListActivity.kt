package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityTransactionsListBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllTransactionsListActivity : BaseActivityWrapper<ActivityTransactionsListBinding, AllTransactionListNavigator, AllTransactionsListViewModel>(),
    AllTransactionListNavigator {

    private val allTransactionsListViewModel: AllTransactionsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, getString(R.string.all_transactions), R.drawable.ic_arrow_left)
        mViewModel?.getGramCashTransactionData()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_transactions_list
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AllTransactionsListViewModel {
        return allTransactionsListViewModel
    }
}