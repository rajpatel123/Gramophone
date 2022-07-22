package agstack.gramophone.ui.offer

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import android.content.Context
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    private val offerDetailRepository: OfferDetailRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<OfferDetailNavigator>() {

    var mOffersItem =ObservableField<PromotionListItem>()



}
