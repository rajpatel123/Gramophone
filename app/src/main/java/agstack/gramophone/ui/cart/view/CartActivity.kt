package agstack.gramophone.ui.cart.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCartBinding
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.model.CartItem
import agstack.gramophone.ui.cart.viewmodel.CartViewModel
import agstack.gramophone.ui.dialog.BottomSheetDialog
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Utility
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.progress
import kotlinx.android.synthetic.main.activity_login.*
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
        toolbar.rl_help.setOnClickListener(View.OnClickListener {
            val bottomSheet = BottomSheetDialog()
            //bottomSheet.setAcceptRejectListener(listener)
            bottomSheet.show(
                supportFragmentManager,
                "bottomSheet"
            )
        })
    }

    override fun setCartAdapter(
        cartAdapter: CartAdapter,
        onCartItemClicked: (CartItem) -> Unit,
        onCartItemDeleteClicked: (String) -> Unit,
        onOfferClicked: (String) -> Unit,
    ) {
        cartAdapter.onItemDetailClicked = onCartItemClicked
        cartAdapter.onItemDeleteClicked = onCartItemDeleteClicked
        cartAdapter.onOfferClicked = onOfferClicked
        rvCart?.adapter = cartAdapter
    }

    override fun openProductDetailsActivity() {
        openActivity(ProductDetailsActivity::class.java)
    }

    override fun openAppliedOfferDetailActivity() {

    }

    override fun deleteCartItem(productId: String) {
        showToast("delete in progress")
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