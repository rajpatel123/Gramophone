package agstack.gramophone.ui.home.view.fragments.market

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.ui.home.repository.HomeRepository
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MarketFragmentViewModel
@Inject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel<MarketFragmentNavigator>() {

}