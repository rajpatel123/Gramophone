package agstack.gramophone.ui.offerslist


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.OffersListActivityBinding
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OffersListActivity :
    BaseActivityWrapper<OffersListActivityBinding, OffersListNavigator, OffersListViewModel>(),
    OffersListNavigator {

    private val offersListViewModel: OffersListViewModel by viewModels()

    override fun getLayoutID(): Int {
        return R.layout.offers_list_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): OffersListViewModel {
        return offersListViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.all_offers), R.drawable.ic_arrow_left)
        getAllOffersData()
    }

    private fun getAllOffersData() {
        mViewModel?.getAllOffersData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflator = menuInflater
        inflator.inflate(R.menu.menu_search, menu)
        // Material Search Implementation
        val menuItem = menu.findItem(R.id.item_search)
        viewDataBinding.searchView?.setMenuItem(menuItem)

        return super.onCreateOptionsMenu(menu)

    }

    /*  override fun onOptionsItemSelected(item: MenuItem): Boolean {
          when (item.itemId) {
              R.id.item_search -> {

              }

          }
          return true
      }*/

}