package agstack.gramophone.ui.offerslist


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.OffersListActivityBinding
import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.ui.referandearn.transaction.GramCashTransactionListAdapter
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.EndlessRecyclerScrollListener
import agstack.gramophone.utils.LocaleManagerClass
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OffersListActivity :
    BaseActivityWrapper<OffersListActivityBinding, OffersListNavigator, OffersListViewModel>(),
    OffersListNavigator {

    private val offersListViewModel: OffersListViewModel by viewModels()
    private lateinit var listener: EndlessRecyclerScrollListener

    override fun getLayoutID(): Int {
        return R.layout.offers_list_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OffersListViewModel {
        return offersListViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.all_offers), R.drawable.ic_arrow_left)
        getAllOffersData()
    }

    private fun getAllOffersData() {

        viewDataBinding?.swipeRefresh?.setColorSchemeResources(R.color.blue)
        viewDataBinding?.swipeRefresh?.setOnRefreshListener {
            mViewModel?.getAllOffersData()
            viewDataBinding?.swipeRefresh?.isRefreshing = false
        }

        mViewModel?.getAllOffersData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.menu_search, menu)
        // Material Search Implementation
        val menuItem = menu.findItem(R.id.item_search)
        viewDataBinding.searchView?.setMenuItem(menuItem)

        return super.onCreateOptionsMenu(menu)

    }


    override fun setOffersListAdapter(
        offersListAdapter: OffersListAdapter,
        onOfferSelected: (DataItem) -> Unit
    ) {
        viewDataBinding.rvOffersList.adapter = offersListAdapter
        offersListAdapter.selectedOffer = onOfferSelected
        setScrollListenerOnAllOffersList()
    }

    private fun setScrollListenerOnAllOffersList() {
        listener = object : EndlessRecyclerScrollListener(
            viewDataBinding.rvOffersList.layoutManager as LinearLayoutManager,
            { mViewModel?.loadMore(it) }) {
            override fun isLastPage(): Boolean {
                return mViewModel?.isLastPage ?: false
            }

        }
        viewDataBinding.rvOffersList.addOnScrollListener(listener)

    }

    override fun onListUpdated() {
        listener.onListFetched()
        (viewDataBinding.rvOffersList.adapter as OffersListAdapter).hideLoadingItem()
        viewDataBinding.rvOffersList.adapter?.notifyDataSetChanged()
    }

    override fun showLoaderFooter() {
        (viewDataBinding.rvOffersList.adapter as OffersListAdapter).showLoadingItem()
        viewDataBinding.rvOffersList.adapter?.notifyDataSetChanged()
    }

    override fun getLanguageCode(): String? = LocaleManagerClass.getLangCodeAsPerAppLocale(this)

    override fun ShowNoListView(showNoItemView: Boolean) {
        if (showNoItemView) {
            viewDataBinding.llNoOffers.visibility = View.VISIBLE
            viewDataBinding.rvOffersList.visibility = View.GONE
        } else {
            viewDataBinding.llNoOffers.visibility = View.GONE
            viewDataBinding.rvOffersList.visibility = View.VISIBLE
        }
    }


}