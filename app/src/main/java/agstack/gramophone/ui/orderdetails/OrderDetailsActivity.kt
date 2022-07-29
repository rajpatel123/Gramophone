package agstack.gramophone.ui.orderdetails


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderDetailsBinding
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.OfferApplied
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
        toolbar.tvTitle.text = getString(R.string.my_order)
        toolbar.flBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        toolbar.rlHelp.setOnClickListener(View.OnClickListener {
            val bottomSheet = BottomSheetDialog()
            //bottomSheet.setAcceptRejectListener(listener)
            bottomSheet.show(
                supportFragmentManager,
                "bottomSheet"
            )
        })
    }

    override fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (offerList: List<OfferApplied>) -> Unit,
    ) {
        orderedProductsAdapter.onItemDetailClicked = onOrderItemClicked
        orderedProductsAdapter.onOfferClicked = onOfferClicked
        rvOrderList?.adapter = orderedProductsAdapter
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