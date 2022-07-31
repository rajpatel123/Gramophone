package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.viewmodel.OrderListViewModel
import agstack.gramophone.ui.orderdetails.OrderDetailsActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*


@AndroidEntryPoint
class OrderListActivity :
    BaseActivityWrapper<ActivityOrderListBinding, OrderListNavigator, OrderListViewModel>(),
    OrderListNavigator {

    //initialise ViewModel
    private val orderListViewModel: OrderListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        orderListViewModel.getOrderList()
    }

    private fun setupUi() {
        viewDataBinding.toolbar.tvTitle.text = getString(R.string.my_orders)
        viewDataBinding.toolbar.tvHelp.setTextColor(ContextCompat.getColor(this, R.color.orange))
        viewDataBinding.toolbar.ivCall.setColorFilter(ContextCompat.getColor(this, R.color.orange),
            PorterDuff.Mode.SRC_IN)
        viewDataBinding.toolbar.flBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        viewDataBinding.toolbar.rlHelp.setOnClickListener(View.OnClickListener {
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
        })
        viewDataBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (viewDataBinding.tabLayout.selectedTabPosition == 0) {
                    orderListViewModel.selectedTab.value = 0
                    orderListViewModel.emptyText.value = getString(R.string.no_recent_order)
                } else if (viewDataBinding.tabLayout.selectedTabPosition == 1) {
                    orderListViewModel.selectedTab.value = 1
                    orderListViewModel.emptyText.value = getString(R.string.no_past_order)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        } as OnTabSelectedListener)
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