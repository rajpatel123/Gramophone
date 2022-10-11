package agstack.gramophone.ui.offer


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.OfferDetailsActivityBinding
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offerslist.model.DataItem
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
public class OfferDetailActivity :
    BaseActivityWrapper<OfferDetailsActivityBinding, OfferDetailNavigator, OfferDetailViewModel>(),
    OfferDetailNavigator {

    private val offerDetailViewModel: OfferDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getParcelable<PromotionListItem>(Constants.OFFERSDATA) != null) {
                //Check when flowing from Product Details as Model is changed , PromotionListItem->DataItem
              mViewModel?.mOffersItem?.set(bundle.getParcelable<DataItem>(Constants.OFFERSDATA))
            }

            if (bundle.getParcelable<DataItem>(Constants.OFFERSDATA_OFFERSLIST) != null) {
                mViewModel?.mOffersItem?.set(bundle.getParcelable<DataItem>(Constants.OFFERSDATA_OFFERSLIST))
            }
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    private fun setupUi() {
        setUpToolBar(true, resources.getString(R.string.offer_detail), R.drawable.ic_arrow_left)
    }

    override fun getLayoutID(): Int {
        return R.layout.offer_details_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OfferDetailViewModel {
        return offerDetailViewModel
    }

}