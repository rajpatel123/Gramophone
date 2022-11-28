package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchProductsBinding
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.search.adapter.ProductsAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewAllProductsActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchProductsBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        viewDataBinding.recyclerViewProducts.layoutManager = gridLayoutManager

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.recyclerViewProducts.adapter = ProductsAdapter(displayDataList) {
            openActivity(this@ViewAllProductsActivity,
                ProductDetailsActivity::class.java,
                Bundle().apply {
                    putParcelable(
                        Constants.PRODUCT,
                        ProductData(it)
                    )
                })
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_products
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "products" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.recyclerViewProducts.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}