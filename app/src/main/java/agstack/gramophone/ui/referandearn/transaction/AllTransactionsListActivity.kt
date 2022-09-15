package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityTransactionsListBinding
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllTransactionsListActivity : BaseActivityWrapper<ActivityTransactionsListBinding, AllTransactionListNavigator, AllTransactionsListViewModel>(),
    AllTransactionListNavigator {

    private val allTransactionsListViewModel: AllTransactionsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, getString(R.string.all_transactions), R.drawable.ic_arrow_left)
        mViewModel?.getGramCashTransactionData()
        initViews()
    }

    private fun initViews() {
        viewDataBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (viewDataBinding.tabLayout.selectedTabPosition == 0) {
                    mViewModel?.selectedTab?.set(0)
                    mViewModel?.emptyText?.set( getString(R.string.no_transactions))
                } else if (viewDataBinding.tabLayout.selectedTabPosition == 1) {
                    mViewModel?.selectedTab?.set(1)
                    mViewModel?.emptyText?.set( getString(R.string.no_referral_transactions))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        } as TabLayout.OnTabSelectedListener)
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

    override fun setAllTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter) {
        viewDataBinding.rvAll.adapter= gramCashTransactionListAdapter
    }

    override fun setReferralTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter) {
        viewDataBinding.rvReferral.adapter= gramCashTransactionListAdapter
    }
}