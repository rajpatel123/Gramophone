package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.databinding.ActivityViewAllSearchCompaniesBinding
import agstack.gramophone.ui.search.adapter.CompaniesAdapter
import agstack.gramophone.ui.search.adapter.CropProblemsAdapter
import agstack.gramophone.ui.search.model.Data
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amnix.xtension.extensions.toCamelCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllSearchCompaniesActivity :
    BaseActivityWrapper<ActivityViewAllSearchCompaniesBinding, BaseNavigator, BaseViewModel<BaseNavigator>>(),
    BaseNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val items = dataList?.items
        items?.let {
            val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
            viewDataBinding.rvCompanies.layoutManager = gridLayoutManager
            viewDataBinding.rvCompanies.adapter = CompaniesAdapter(items){}
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_companies
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): BaseViewModel<BaseNavigator> {
        val viewModel: BaseViewModel<BaseNavigator> by viewModels()
        return viewModel
    }
}