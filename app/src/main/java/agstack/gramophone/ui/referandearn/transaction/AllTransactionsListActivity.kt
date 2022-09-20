package agstack.gramophone.ui.referandearn.transaction

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityTransactionsListBinding
import agstack.gramophone.ui.home.product.activity.RatingAndReviewsAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.EndlessRecyclerScrollListener
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllTransactionsListActivity : BaseActivityWrapper<ActivityTransactionsListBinding, AllTransactionListNavigator, AllTransactionsListViewModel>(),
    AllTransactionListNavigator {

    private val allTransactionsListViewModel: AllTransactionsListViewModel by viewModels()
    private lateinit var listener: EndlessRecyclerScrollListener

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
        setScrollListenerOnAllTxnList()
    }


    override fun setReferralTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter) {
        viewDataBinding.rvReferral.adapter= gramCashTransactionListAdapter
        setScrollListenerOnReferralTxnList()
    }


    private fun setScrollListenerOnAllTxnList() {
        listener = object : EndlessRecyclerScrollListener(
            viewDataBinding.rvAll.layoutManager as LinearLayoutManager,
            { mViewModel?.loadMore(it,Constants.ALL_STRING) }) {
            override fun isLastPage(): Boolean {
                return mViewModel?.isLastPage ?: false
            }

        }
        viewDataBinding.rvAll.addOnScrollListener(listener)

    }
    override fun showLoaderFooter() {
        (viewDataBinding.rvAll.adapter as GramCashTransactionListAdapter).showLoadingItem()
        viewDataBinding.rvAll.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdated() {
        listener.onListFetched()
        (viewDataBinding.rvAll.adapter as GramCashTransactionListAdapter).hideLoadingItem()
        viewDataBinding.rvAll.adapter?.notifyDataSetChanged()
    }



    private fun setScrollListenerOnReferralTxnList() {
        listener = object : EndlessRecyclerScrollListener(
            viewDataBinding.rvReferral.layoutManager as LinearLayoutManager,
            { mViewModel?.loadMore(it,Constants.REFERRAL) }) {
            override fun isLastPage(): Boolean {
                return mViewModel?.isLastPageReferral ?: false
            }

        }
        viewDataBinding.rvReferral.addOnScrollListener(listener)

    }

    override fun showLoaderFooterReferral() {
        (viewDataBinding.rvReferral.adapter as GramCashTransactionListAdapter).showLoadingItem()
        viewDataBinding.rvReferral.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdatedReferral() {
        listener.onListFetched()
        (viewDataBinding.rvReferral.adapter as GramCashTransactionListAdapter).hideLoadingItem()
        viewDataBinding.rvReferral.adapter?.notifyDataSetChanged()
    }



}