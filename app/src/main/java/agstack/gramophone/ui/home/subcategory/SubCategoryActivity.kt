package agstack.gramophone.ui.home.subcategory


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityTestCollapseBinding
import agstack.gramophone.databinding.ActivityWeatherBinding
import agstack.gramophone.ui.dialog.LocationAccessDialog
import agstack.gramophone.ui.home.adapter.ProductListAdapter
import agstack.gramophone.ui.home.subcategory.adapter.SubCategoryAdapter
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryActivity :
    BaseActivityWrapper<ActivityTestCollapseBinding, SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator, View.OnClickListener {

    //initialise ViewModel
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setUpToolBar(true, "Crop Nutrition", R.drawable.ic_arrow_left)
        subCategoryViewModel.setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_share, menu);
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item?.itemId == R.id.itemShare) {
                item.actionView?.setOnClickListener(this)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(view: View?) {
        /*when (view?.id) {
            R.id.itemShare -> {

            }
            R.id.tvChangeLoc -> {
                val locationAccessDialog = LocationAccessDialog()
                locationAccessDialog.listener = this
                locationAccessDialog.show(
                    supportFragmentManager,
                    Constants.LOCATION_ACCESS_DIALOG
                )
            }
        }*/
    }

    override fun setSubCategoryAdapter(subCategoryAdapter: SubCategoryAdapter) {
        viewDataBinding.rvSubCategory.adapter = subCategoryAdapter
    }

    override fun setProductListAdapter(productListAdapter: ProductListAdapter) {
        viewDataBinding.rvProducts.adapter = productListAdapter
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_test_collapse
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): SubCategoryViewModel {
        return subCategoryViewModel
    }

}