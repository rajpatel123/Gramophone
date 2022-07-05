package agstack.gramophone.ui.cart.view


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCartBinding
import agstack.gramophone.ui.cart.CartNavigator
import agstack.gramophone.ui.cart.adapter.CartAdapter
import agstack.gramophone.ui.cart.viewmodel.CartViewModel
import agstack.gramophone.ui.dialog.BottomSheetDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
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
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        rv_cart?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_cart?.setHasFixedSize(true)
        rv_cart?.adapter = CartAdapter()

        binding.toolbar.rlHelp.setOnClickListener(View.OnClickListener {
            val bottomSheet = BottomSheetDialog()
            //bottomSheet.setAcceptRejectListener(listener)
            bottomSheet.show(
                getSupportFragmentManager(),
                "bottomSheet"
            )
        })
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