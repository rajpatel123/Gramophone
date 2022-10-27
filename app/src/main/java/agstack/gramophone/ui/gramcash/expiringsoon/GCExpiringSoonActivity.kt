package agstack.gramophone.ui.gramcash.expiringsoon


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.GramcashexpiringsoonActivityBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.transaction.GramCashTransactionListAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.EndlessRecyclerScrollListener
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GCExpiringSoonActivity :
    BaseActivityWrapper<GramcashexpiringsoonActivityBinding, GCExpiringSoonNavigator, GCExpiringSoonViewModel>(),
    GCExpiringSoonNavigator {
    private lateinit var listener: EndlessRecyclerScrollListener
    var GC_Expiring_soon_amount:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.points_expiring), R.drawable.ic_arrow_left)

        val bundle = getBundle()
        bundle?.let {

            if (bundle.getString(Constants.GC_Expiring_soon) != null) {
                GC_Expiring_soon_amount   = bundle.getString(Constants.GC_Expiring_soon)
            }
        }
        mViewModel?.getGramCashTxnExpiringSoon(GC_Expiring_soon_amount)
    }
    fun getBundle(): Bundle? {
        return intent.extras
    }

    // putString(Constants.GC_Expiring_soon,gramCashResponseData.get()?.gramcashExpiringSoon.toString())

    private val gcExpiringSoonViewModel: GCExpiringSoonViewModel by viewModels()
    override fun getLayoutID(): Int {
        return R.layout.gramcashexpiringsoon_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): GCExpiringSoonViewModel {
        return gcExpiringSoonViewModel
    }

    override fun setExpireTxnListAdapter(gramCashTransactionListAdapter: GramCashTransactionListAdapter) {
        viewDataBinding.rvexpireSoon.adapter = gramCashTransactionListAdapter
        setScrollListenerOnTxnList()
    }

    private fun setScrollListenerOnTxnList() {
        listener = object : EndlessRecyclerScrollListener(
            viewDataBinding.rvexpireSoon.layoutManager as LinearLayoutManager,
            { mViewModel?.loadMore(it) }) {
            override fun isLastPage(): Boolean {
                return mViewModel?.isLastPage ?: false
            }

        }
        viewDataBinding.rvexpireSoon.addOnScrollListener(listener)

    }

    override fun onListUpdated() {
        listener.onListFetched()
        (viewDataBinding.rvexpireSoon.adapter as GramCashTransactionListAdapter).hideLoadingItem()
        viewDataBinding.rvexpireSoon.adapter?.notifyDataSetChanged()
    }

    override fun showLoaderFooter() {
        (viewDataBinding.rvexpireSoon.adapter as GramCashTransactionListAdapter).showLoadingItem()
        viewDataBinding.rvexpireSoon.adapter?.notifyDataSetChanged()
    }

    override fun openHomeActivity() {
        openAndFinishActivityWithClearTopNewTaskClearTaskFlags(HomeActivity::class.java, null)
    }

}
