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
import agstack.gramophone.utils.ApiResponse
import agstack.gramophone.utils.Utility
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.*

@AndroidEntryPoint
class CartActivity : BaseActivityWrapper<ActivityCartBinding, CartNavigator, CartViewModel>(),
    CartNavigator {

    private lateinit var binding: ActivityCartBinding

    //initialise ViewModel
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
        cartViewModel.getCartData()
    }

    private fun setupObservers() {
        cartViewModel.getCartDataResponse.observe(this, Observer {
            when (it) {
                is ApiResponse.Success -> {
                    progress.visibility = View.GONE
                    Utility.showShortToast(this@CartActivity, it.data?.gp_api_message)
                    var cartItems = it.data?.gp_api_response_data?.cart_items
                    if (cartItems == null) cartItems = ArrayList()
                    setCartAdapter(CartAdapter(cartItems)) {
                        Utility.showShortToast(this@CartActivity, getString(R.string.add_to_cart))
                    }
                }
                is ApiResponse.Error -> {
                    progress.visibility = View.GONE
                    Utility.showShortToast(this@CartActivity, it.message)
                }
                is ApiResponse.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun setupUi() {
        /*rv_cart?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_cart?.setHasFixedSize(true)
        rv_cart?.adapter = CartAdapter()*/

        binding.toolbar.rlHelp.setOnClickListener(View.OnClickListener {
            val bottomSheet = BottomSheetDialog()
            //bottomSheet.setAcceptRejectListener(listener)
            bottomSheet.show(
                getSupportFragmentManager(),
                "bottomSheet"
            )
        })
    }

     private fun setCartAdapter(cartAdapter: CartAdapter, onCartItemClicked: (CartItem) -> Unit) {
        cartAdapter.selectedProduct = onCartItemClicked
        rv_cart?.adapter = cartAdapter
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