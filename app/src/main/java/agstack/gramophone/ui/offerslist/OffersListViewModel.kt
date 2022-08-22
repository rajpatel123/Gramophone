package agstack.gramophone.ui.offerslist

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.settings.SettingsRepository
import agstack.gramophone.ui.home.view.fragments.market.model.PromotionListItem
import agstack.gramophone.ui.offerslist.adapter.OffersListAdapter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OffersListViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<OffersListNavigator>() , MaterialSearchView.OnQueryTextListener,
    MaterialSearchView.SearchViewListener {

    var allOfferslist:List<PromotionListItem> = ArrayList()

    fun getAllOffersData() {
       getNavigator()?.setOffersListAdapter(OffersListAdapter(allOfferslist))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
    return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onSearchViewShown() {

    }

    override fun onSearchViewClosed() {

    }


}
