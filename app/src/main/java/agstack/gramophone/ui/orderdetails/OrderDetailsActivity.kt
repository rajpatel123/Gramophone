package agstack.gramophone.ui.orderdetails


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderDetailsBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.OfferApplied
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.activity_order_list.toolbar
import kotlinx.android.synthetic.main.toolbar_with_back_arrow_and_help.view.*

@AndroidEntryPoint
class OrderDetailsActivity :
    BaseActivityWrapper<ActivityOrderDetailsBinding, OrderDetailsNavigator, OrderDetailsViewModel>(),
    OrderDetailsNavigator {
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailsViewModel.getBundleData()
        setupUi()
    }

    private fun setupUi() {
        viewDataBinding.toolbar.tvTitle.text = getString(R.string.my_order)
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

    override fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (offerList: List<OfferApplied>) -> Unit,
    ) {
        orderedProductsAdapter.onItemDetailClicked = onOrderItemClicked
        orderedProductsAdapter.onOfferClicked = onOfferClicked
        viewDataBinding.rvOrderList.adapter = orderedProductsAdapter
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_order_details
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OrderDetailsViewModel {
        return orderDetailsViewModel
    }

}