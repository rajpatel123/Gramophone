package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.viewmodel.OrderListViewModel
import agstack.gramophone.ui.orderdetails.OrderDetailsActivity
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import com.amnix.xtension.extensions.isNotNull
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderListActivity :
    BaseActivityWrapper<ActivityOrderListBinding, OrderListNavigator, OrderListViewModel>(),
    OrderListNavigator, View.OnClickListener {

    //initialise ViewModel
    private val orderListViewModel: OrderListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        orderListViewModel.getOrders()
    }

    private fun setupUi() {
        sendViewRecentMoEngageEvent()
        setUpToolBar(true, getString(R.string.my_orders), R.drawable.ic_arrow_left)
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            orderListViewModel.getOrders()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
        viewDataBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (viewDataBinding.tabLayout.selectedTabPosition == 0) {
                    sendViewRecentMoEngageEvent()
                    orderListViewModel.selectedTab.value = 0
                    orderListViewModel.emptyText.value = getString(R.string.no_recent_order)
                } else if (viewDataBinding.tabLayout.selectedTabPosition == 1) {
                    sendViewPastMoEngageEvent()
                    orderListViewModel.selectedTab.value = 1
                    orderListViewModel.emptyText.value = getString(R.string.no_past_order)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        } as OnTabSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_order, menu);
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item?.itemId == R.id.itemOrder) {
                item.actionView?.setOnClickListener(this)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.itemOrder -> {
                orderListViewModel.onHelpClicked()
            }
        }
    }

    override fun setPlacedOrderAdapter(
        adapter: OrderListAdapter,
        onOrderItemClick: (String, String) -> Unit,
    ) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvPlaced.adapter = adapter
    }

    override fun setRecentOrderAdapter(
        adapter: OrderListAdapter,
        onOrderItemClick: (String, String) -> Unit,
    ) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvRecent.adapter = adapter

    }

    override fun setPastOrderAdapter(
        adapter: OrderListAdapter,
        onOrderItemClick: (String, String) -> Unit,
    ) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvPast.adapter = adapter
    }

    override fun openOrderDetailsActivity(bundle: Bundle) {
        openActivity(OrderDetailsActivity::class.java, bundle)
    }

    override fun openHomeActivity() {
        openAndFinishActivityWithClearTopNewTaskClearTaskFlags(HomeActivity::class.java, null)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_order_list
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OrderListViewModel {
        return orderListViewModel
    }

    private fun sendViewRecentMoEngageEvent() {
        val properties = Properties()
        properties
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Recent_Orders", properties)
    }

    private fun sendViewPastMoEngageEvent() {
        val properties = Properties()
        properties
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_View_Past_Orders", properties)
    }

}