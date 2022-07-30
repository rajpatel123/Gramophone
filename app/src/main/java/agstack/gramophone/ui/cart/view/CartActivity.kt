package agstack.gramophone.ui.cart.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCartBinding
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.model.OfferApplied
import agstack.gramophone.ui.cart.viewmodel.CartViewModel
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.progress
import kotlinx.android.synthetic.main.activity_cart.view.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow_and_help.view.*

@AndroidEntryPoint
class CartActivity : BaseActivityWrapper<ActivityCartBinding, CartNavigator, CartViewModel>(),
    CartNavigator {

    //initialise ViewModel
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        cartViewModel.getCartData()
    }

    private fun setupUi() {
        viewDataBinding.toolbar.tvTitle.text = getString(R.string.cart)
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

    override fun setCartAdapter(
        cartAdapter: CartAdapter,
        onItemDetailClicked: (productId: String) -> Unit,
        onCartItemDeleteClicked: (productId: String) -> Unit,
        onOfferClicked: (offerAppliedList: List<OfferApplied>) -> Unit,
        onQuantityClicked: (cartItem: CartItem) -> Unit,
    ) {
        cartAdapter.onItemDetailClicked = onItemDetailClicked
        cartAdapter.onItemDeleteClicked = onCartItemDeleteClicked
        cartAdapter.onOfferClicked = onOfferClicked
        cartAdapter.onQuantityClicked = onQuantityClicked
        viewDataBinding.rvCart.adapter = cartAdapter
    }

    override fun openProductDetailsActivity(productData: ProductData) {
        openActivity(ProductDetailsActivity::class.java)
    }

    override fun openAppliedOfferDetailActivity(offerAppliedList: List<OfferApplied>) {

    }

    override fun onLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress.visibility = View.GONE
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

}