package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchCropCategoriesBinding
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.search.adapter.CropsCategoriesAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllCropCategoriesActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchCropCategoriesBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        viewDataBinding.rvCropCategory.layoutManager = gridLayoutManager

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.rvCropCategory.adapter =
            CropsCategoriesAdapter(displayDataList) { id, subId, name, image ->
                openActivity(this@ViewAllCropCategoriesActivity,
                    FeaturedProductActivity::class.java,
                    Bundle().apply {
                        putString(Constants.SHOP_BY_SUB_CATEGORY, id)
                        putString(Constants.SUB_CATEGORY_ID, subId)
                        putString(Constants.SUB_CATEGORY_NAME, name)
                        putString(Constants.SUB_CATEGORY_IMAGE, image)
                    })
            }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_crop_categories
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "crop_categories" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.rvCropCategory.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}