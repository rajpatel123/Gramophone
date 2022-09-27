package agstack.gramophone.ui.offer

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offerslist.model.DataItem
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(

) : BaseViewModel<OfferDetailNavigator>() {

    var mOffersItem =ObservableField<DataItem>()



}
