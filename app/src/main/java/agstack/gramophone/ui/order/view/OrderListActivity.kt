package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.product.activity.ProductAllReviewsActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow_and_help.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
        viewDataBinding.toolbar.ivCall.setColorFilter(ContextCompat.getColor(this, R.color.orange), PorterDuff.Mode.SRC_IN)
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
    }

    override fun setOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String) -> Unit) {
        adapter.onOrderDetailClicked = onOrderItemClick
        viewDataBinding.rvOrder.adapter = adapter

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