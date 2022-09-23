package agstack.gramophone.ui.orderdetails


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderDetailsBinding
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.ui.orderdetails.model.OfferApplied
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsActivity :
    BaseActivityWrapper<ActivityOrderDetailsBinding, OrderDetailsNavigator, OrderDetailsViewModel>(),
    OrderDetailsNavigator, View.OnClickListener {
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailsViewModel.getBundleData()
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.my_order), R.drawable.ic_arrow_left)
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
                orderDetailsViewModel.onHelpClick()
            }
        }
    }

    override fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (offerList: OfferApplied) -> Unit,
    ) {
        orderedProductsAdapter.onItemDetailClicked = onOrderItemClicked
        orderedProductsAdapter.onOfferClicked = onOfferClicked
        viewDataBinding.rvOrderList.adapter = orderedProductsAdapter
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable("product", productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
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