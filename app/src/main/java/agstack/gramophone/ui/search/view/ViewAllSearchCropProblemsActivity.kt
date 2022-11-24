package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.databinding.ActivityViewAllSearchCropProblemsBinding
import agstack.gramophone.ui.advisory.view.CropProblemDetailActivity
import agstack.gramophone.ui.home.subcategory.SubCategoryActivity
import agstack.gramophone.ui.search.adapter.CropProblemsAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amnix.xtension.extensions.toCamelCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllSearchCropProblemsActivity :
    BaseActivityWrapper<ActivityViewAllSearchCropProblemsBinding, BaseNavigator, BaseViewModel<BaseNavigator>>(),
    BaseNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val items = dataList?.items
        items?.let {
            val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            viewDataBinding.rvCropProblems.layoutManager = gridLayoutManager
            viewDataBinding.rvCropProblems.adapter = CropProblemsAdapter(items){id, name, image,desc,type ->
                openActivity(
                    CropProblemDetailActivity::class.java,
                    Bundle().apply {
                        putInt(Constants.DESEASE_ID,id.toInt())
                        putString(Constants.DESEASE_NAME,name)
                        putString(Constants.DESEASE_DESC,desc)
                        putString(Constants.DESEASE_IMAGE,image)
                        putString(Constants.DESEASE_TYPE,type)
                    }
                )
            }
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_crop_problems
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): BaseViewModel<BaseNavigator> {
        val viewModel: BaseViewModel<BaseNavigator> by viewModels()
        return viewModel
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }
}