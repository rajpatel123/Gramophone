package agstack.gramophone.ui.offer

import agstack.gramophone.base.BaseViewModel
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    private val offerDetailRepository: OfferDetailRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<OfferDetailNavigator>() {



}
