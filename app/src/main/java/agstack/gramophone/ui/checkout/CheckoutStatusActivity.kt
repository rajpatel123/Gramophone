package agstack.gramophone.ui.checkout


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCheckoutStatusBinding
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*

@AndroidEntryPoint
class CheckoutStatusActivity : BaseActivityWrapper<ActivityCheckoutStatusBinding, CheckoutStatusNavigator, CheckoutStatusViewModel>(), CheckoutStatusNavigator {

    private lateinit var binding: ActivityCheckoutStatusBinding
    //initialise ViewModel
    private val checkoutStatusViewModel: CheckoutStatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {

    }


    override fun getLayoutID(): Int {
      return R.layout.activity_checkout_status
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): CheckoutStatusViewModel {
        return checkoutStatusViewModel
    }

}