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

    var allOfferslist:MutableList<PromotionListItem> = ArrayList()

    fun getAllOffersData() {
        var item= PromotionListItem(1,1,"50% Off on Pesticide",null,null,0.0,false,"Test","Valid on ","Valid till")

    allOfferslist.add(item)
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
