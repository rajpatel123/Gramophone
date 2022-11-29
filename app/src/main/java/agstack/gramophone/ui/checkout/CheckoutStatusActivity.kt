package agstack.gramophone.ui.checkout


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCheckoutStatusBinding
import agstack.gramophone.ui.order.view.OrderListActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutStatusActivity : BaseActivityWrapper<ActivityCheckoutStatusBinding, CheckoutStatusNavigator, CheckoutStatusViewModel>(), CheckoutStatusNavigator {

    //initialise ViewModel
    private val checkoutStatusViewModel: CheckoutStatusViewModel by viewModels()
    private var lastTimeClicked: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutStatusViewModel.getBundleData()
    }

    override fun openOrderListActivity() {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < 2000) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        openAndFinishActivity(OrderListActivity::class.java, null)
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
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