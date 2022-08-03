package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.viewmodel.OrderListViewModel
import agstack.gramophone.ui.orderdetails.OrderDetailsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
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
        orderListViewModel.getOrderList()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.my_orders), R.drawable.ic_arrow_left)
        viewDataBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (viewDataBinding.tabLayout.selectedTabPosition == 0) {
                    viewDataBinding.rvRecent.visibility = View.VISIBLE
                    viewDataBinding.rvPast.visibility = View.GONE
                    orderListViewModel.selectedTab.value = 0
                    orderListViewModel.emptyText.value = getString(R.string.no_recent_order)
                } else if (viewDataBinding.tabLayout.selectedTabPosition == 1) {
                    viewDataBinding.rvRecent.visibility = View.GONE
                    viewDataBinding.rvPast.visibility = View.VISIBLE
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
                val supportNo: String? =
                    SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CustomerSupportNo)
                if (supportNo?.isNotEmpty() == true) {
                    val bottomSheet = BottomSheetDialog()
                    bottomSheet.customerSupportNumber = supportNo
                    bottomSheet.show(
                        supportFragmentManager,
                        Constants.BOTTOM_SHEET
                    )
                }
            }
        }
    }

    override fun setRecentOrderAdapter(
        adapter: OrderListAdapter,
        onOrderItemClick: (String) -> Unit,
    ) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvRecent.adapter = adapter

    }

    override fun setPastOrderAdapter(
        adapter: OrderListAdapter,
        onOrderItemClick: (String) -> Unit,
    ) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvPast.adapter = adapter
    }

    override fun openOrderDetailsActivity(bundle: Bundle) {
        openActivity(OrderDetailsActivity::class.java, bundle)
    }

    override fun openHomeActivity() {
        openAndFinishActivityWithFlags(HomeActivity::class.java, null)
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

}