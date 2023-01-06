package agstack.gramophone.ui.cart.view


import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCartBinding
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.GpApiResponseData
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.cart.viewmodel.CartViewModel
import agstack.gramophone.ui.checkout.CheckoutStatusActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.Utility
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.*
import java.lang.StringBuilder


@AndroidEntryPoint
class CartActivity : BaseActivityWrapper<ActivityCartBinding, CartNavigator, CartViewModel>(),
    CartNavigator, View.OnClickListener {

    //initialise ViewModel
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        cartViewModel.getBundleData()
        cartViewModel.getCartData()
    }

    private fun setupUi() {
        setUpToolBar(true, getString(R.string.cart), R.drawable.ic_arrow_left)
        viewDataBinding.llPriceBreakUp.setOnClickListener {
            viewDataBinding.nestedScroll.fullScroll(View.FOCUS_DOWN)
        }
        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            cartViewModel.getCartData()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_cart, menu);
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item?.itemId == R.id.itemCart) {
                item.actionView?.setOnClickListener(this)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.itemCart -> {
                cartViewModel.onHelpClicked()
            }
        }
    }

    override fun setCartAdapter(
        cartAdapter: CartAdapter,
        onItemDetailClicked: (productId: String) -> Unit,
        onCartItemDeleteClicked: (productId: String, cartItem: CartItem) -> Unit,
        onOfferClicked: (offerAppliedList: OfferApplied, productName: String, productSku: String) -> Unit,
        onQuantityClicked: (cartItem: CartItem) -> Unit,
    ) {
        cartAdapter.onItemDetailClicked = onItemDetailClicked
        cartAdapter.onItemDeleteClicked = onCartItemDeleteClicked
        cartAdapter.onOfferClicked = onOfferClicked
        cartAdapter.onQuantityClicked = onQuantityClicked
        viewDataBinding.rvCart.adapter = cartAdapter
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.PRODUCT, productData)
        openActivity(ProductDetailsActivity::class.java, bundle)
    }

    override fun openCheckoutStatusActivity(bundle: Bundle) {
        openAndFinishActivity(CheckoutStatusActivity::class.java, bundle)
    }

    override fun openHomeActivity() {
        openAndFinishActivityWithClearTopNewTaskClearTaskFlags(HomeActivity::class.java, null)
    }

    override fun onLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress.visibility = View.GONE
    }

    override fun getBundle(): Bundle? {
        return intent?.extras
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_cart
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): CartViewModel {
        return cartViewModel
    }

    override fun sendGoToCartMoEngageEvent(redirectionFrom: String) {
        val properties = Properties()
        properties
            .addAttribute("Redirection_Source", redirectionFrom)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Go_To_Cart", properties)
    }

    override fun sendRemoveProductFromCartMoEngageEvent(
        productId: Int,
        productBaseName: String,
        sellingPrice: Float,
        mrpPrice: Float,
        sellingPriceAfterDiscount: Float,
        productSku: String,
        productQuantity: Int,
        offerId: String,
        productDiscount: Float,
    ) {
        val properties = Properties()
        properties.addAttribute("Product_Id", productId)
            .addAttribute("Product_Base_Name", productBaseName)
            .addAttribute("Krishi_App_Selling_Price", sellingPrice)
            .addAttribute("Product_MRP", mrpPrice)
            .addAttribute("Selling_Price_After_Discount", sellingPriceAfterDiscount)
            .addAttribute("Product_SKU", productSku)
            .addAttribute("Product_Quantity", productQuantity)
            .addAttribute("Offer_Id", offerId)
            .addAttribute("Product_Discount", productDiscount)
            .addAttribute("Customer_Id",
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.CUSTOMER_ID)!!)
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Remove_Product_From_Cart", properties)
    }

    override fun sendPlaceOrderMoEngageEvent(
        cartData: GpApiResponseData,
        orderReferenceId: String,
    ) {
        val userData = SharedPreferencesHelper.instance?.getParcelable(
            SharedPreferencesKeys.CUSTOMER_DATA, GpApiResponseProfileData::class.java
        )
        val productIds: StringBuilder = StringBuilder("[")
        for (cartItem in cartData.cart_items) {
            productIds.append(cartItem.product_id).append(", ")
        }
        productIds.append("]")
        val properties = Properties()
        properties
            .addAttribute("Order Reference ID", orderReferenceId)
            .addAttribute("Customer_Id", userData?.customer_id)
            .addAttribute("Customer_Area", userData?.address_data?.tehsil)
            .addAttribute("Customer_Territory", userData?.address_data?.district)
            .addAttribute("Customer_Cluster", userData?.address_data?.district)
            .addAttribute("Conversion_Source", "ANDROID")
            .addAttribute("Order_Date", Utility.getCurrentDate())
            .addAttribute("Total_Order_Value", cartData.total)
            .addAttribute("Total_Discount_Amount", cartData.promotional_discount_total)
            .addAttribute("Gramcash_Used_In_Order", cartData.gramcash_coins)
            .addAttribute("Item_Count_In_Cart", cartData.cart_items.size)
            .addAttribute("Total_Product_Ids_In_Cart", productIds.toString())
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        MoEAnalyticsHelper.trackEvent(this, "KA_Place_Order", properties)
    }
}