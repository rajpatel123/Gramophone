package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchCompaniesBinding
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.search.adapter.CompaniesAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.Item
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllCompaniesActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchCompaniesBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        viewDataBinding.rvCompanies.layoutManager = gridLayoutManager

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.rvCompanies.adapter = CompaniesAdapter(displayDataList) { id, name, image ->
            openActivity(this@ViewAllCompaniesActivity,
                FeaturedProductActivity::class.java,
                Bundle().apply {
                    putString(Constants.COMPANY_ID, id)
                    putString(Constants.COMPANY_NAME, name)
                    putString(Constants.COMPANY_IMAGE, image)
                })
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_companies
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "companies" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.rvCompanies.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}