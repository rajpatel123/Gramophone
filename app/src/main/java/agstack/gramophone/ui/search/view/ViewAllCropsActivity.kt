package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchCropsBinding
import agstack.gramophone.ui.home.cropdetail.CropDetailActivity
import agstack.gramophone.ui.search.adapter.CropsAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllCropsActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchCropsBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        viewDataBinding.rvCrops.layoutManager = gridLayoutManager

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.rvCrops.adapter = CropsAdapter(displayDataList) {
            openActivity(this@ViewAllCropsActivity,
                CropDetailActivity::class.java,
                Bundle().apply {
                    putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_CROP)
                })
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_crops
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "crops" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.rvCrops.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}