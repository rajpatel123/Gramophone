package agstack.gramophone.ui.order.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderListBinding
import agstack.gramophone.ui.order.OrderListNavigator
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.ui.order.viewmodel.OrderListViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*

@AndroidEntryPoint
class OrderListActivity : BaseActivityWrapper<ActivityOrderListBinding, OrderListNavigator, OrderListViewModel>(), OrderListNavigator {

    private lateinit var binding: ActivityOrderListBinding
    //initialise ViewModel
    private val orderListViewModel: OrderListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        rv_order?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_order?.setHasFixedSize(true)
        rv_order?.adapter = OrderListAdapter()
    }


    override fun getLayoutID(): Int {
      return R.layout.activity_order_list
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): OrderListViewModel {
        return orderListViewModel
    }

}