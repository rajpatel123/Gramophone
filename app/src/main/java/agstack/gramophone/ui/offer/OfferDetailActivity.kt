package agstack.gramophone.ui.offer


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityCheckoutStatusBinding
import agstack.gramophone.databinding.ActivityOfferDetailsBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.order.adapter.OrderListAdapter
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_order_list.*

@AndroidEntryPoint
class OfferDetailActivity :
    BaseActivityWrapper<ActivityOfferDetailsBinding, OfferDetailNavigator, OfferDetailViewModel>(),
    OfferDetailNavigator {

    private lateinit var binding: ActivityOfferDetailsBinding

    private val offerDetailViewModel: OfferDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getParcelable<PromotionListItem>(Constants.OFFERSDATA) != null) {
                mViewModel?.mOffersItem?.set(bundle.getParcelable<PromotionListItem>(Constants.OFFERSDATA))

            }
        }


    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    private fun setupUi() {
        setUpToolBar(true, resources.getString(R.string.offer_detail), R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_offer_details
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OfferDetailViewModel {
        return offerDetailViewModel
    }

}