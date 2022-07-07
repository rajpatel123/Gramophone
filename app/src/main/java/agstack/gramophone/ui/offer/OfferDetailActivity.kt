package agstack.gramophone.ui.offer


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCheckoutStatusBinding
import agstack.gramophone.databinding.ActivityOfferDetailsBinding
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*

@AndroidEntryPoint
class OfferDetailActivity : BaseActivityWrapper<ActivityOfferDetailsBinding, OfferDetailNavigator, OfferDetailViewModel>(), OfferDetailNavigator {

    private lateinit var binding: ActivityOfferDetailsBinding
    //initialise ViewModel
    private val offerDetailViewModel : OfferDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        setUpToolBar(true, "Offer Detail", R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
      return R.layout.activity_offer_details
    }

    override fun getBindingVariable(): Int {
        return  BR.viewModel
    }

    override fun getViewModel(): OfferDetailViewModel {
        return offerDetailViewModel
    }

}