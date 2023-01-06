package agstack.gramophone.ui.orderdetails


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityOrderDetailsBinding
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.orderdetails.adapter.OrderedProductsAdapter
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.app.DownloadManager
import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
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
                orderDetailsViewModel.onHelpClicked()
            }
        }
    }

    override fun setColorOnOrderStatus(orderStatus: String) {
        when (orderStatus) {
            "Order Placed" -> {
                viewDataBinding.rlOrderStatus.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.order_placed_bg))
                viewDataBinding.tvOrderStatus.setTextColor(ContextCompat.getColor(this,
                    R.color.order_placed_text))
                viewDataBinding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(this, R.drawable.ic_order_placed),
                    null,
                    null,
                    null
                )
            }
            "Order Approved" -> {
                viewDataBinding.rlOrderStatus.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.order_approved_bg))
                viewDataBinding.tvOrderStatus.setTextColor(ContextCompat.getColor(this,
                    R.color.order_approved_text))
                viewDataBinding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(this, R.drawable.ic_order_approved),
                    null,
                    null,
                    null
                )
            }
            "Order Cancelled" -> {
                viewDataBinding.rlOrderStatus.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.order_cancelled_bg))
                viewDataBinding.tvOrderStatus.setTextColor(ContextCompat.getColor(this,
                    R.color.order_cancelled_text))
                viewDataBinding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(this, R.drawable.ic_order_cancelled),
                    null,
                    null,
                    null
                )
            }
            "Delivered" -> {
                viewDataBinding.rlOrderStatus.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.order_delivered_bg))
                viewDataBinding.tvOrderStatus.setTextColor(ContextCompat.getColor(this,
                    R.color.order_delivered_text))
                viewDataBinding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(this, R.drawable.ic_order_delivered),
                    null,
                    null,
                    null
                )
                viewDataBinding.viewOrderStatus.visibility = View.VISIBLE
                viewDataBinding.tvInvoice.visibility = View.VISIBLE
            }
            "Order Dispatched" -> {
                viewDataBinding.rlOrderStatus.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this,
                        R.color.order_dispatched_bg))
                viewDataBinding.tvOrderStatus.setTextColor(ContextCompat.getColor(this,
                    R.color.order_dispatched_text))
                viewDataBinding.tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(
                    AppCompatResources.getDrawable(this, R.drawable.ic_order_dispatched),
                    null,
                    null,
                    null
                )
            }
        }
    }

    override fun setOrderListAdapter(
        orderedProductsAdapter: OrderedProductsAdapter,
        onOrderItemClicked: (Int) -> Unit,
        onOfferClicked: (OfferApplied, String, String) -> Unit,
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

    override fun downloadInvoice(invoiceUrl: String) {
        sendDownloadInvoiceMoEngageEvent()
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val getPdf = DownloadManager.Request(Uri.parse(invoiceUrl))
        getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(getPdf)
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

    override fun sendOrderDetailsMoEngageEvent(
        orderId: String,
        orderDate: String,
        totalOrderValue: Float,
        orderStatus: String,
    ) {
        val properties = Properties()
        properties.addAttribute("Order_Id", orderId)
            .addAttribute("Order_Date", orderDate)
            .addAttribute("Total_Order_Value", totalOrderValue)
            .addAttribute("Order_Status", orderStatus)
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Order_Details", properties)
    }

    private fun sendDownloadInvoiceMoEngageEvent() {
        val properties = Properties()
        properties.addAttribute("Order_Id", orderDetailsViewModel.orderId.value)
            .addAttribute("Order_Date", orderDetailsViewModel.orderDate.value)
            .addAttribute("Total_Order_Value", orderDetailsViewModel.totalPrice.value)
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Invoice_Downloaded", properties)
    }

}