package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.home.product.activity.ProductAllReviewsActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.viewmodel.OrderListViewModel
import agstack.gramophone.ui.orderdetails.OrderDetailsActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderListActivity :
    BaseActivityWrapper<ActivityOrderListBinding, OrderListNavigator, OrderListViewModel>(),
    OrderListNavigator {

    private lateinit var binding: ActivityOrderListBinding

    //initialise ViewModel
    private val orderListViewModel: OrderListViewModel by viewModels()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        orderListViewModel.getOrderList()
    }

    private fun setupUi() {
        rv_order?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_order?.setHasFixedSize(true)
    }

    override fun setOrderAdapter(adapter: OrderListAdapter, onOrderItemClick: (String) -> Unit) {
        adapter.onOrderDetailClicked = onOrderItemClick
        rv_order?.adapter = adapter

    }

    override fun openOrderDetailsActivity(bundle: Bundle) {
        openActivity(OrderDetailsActivity::class.java,bundle)
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