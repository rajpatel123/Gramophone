package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.databinding.ActivityViewAllSearchProductsBinding
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.search.adapter.ProductsAdapter
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
class ViewAllSearchProductsActivity :
    BaseActivityWrapper<ActivityViewAllSearchProductsBinding, BaseNavigator, BaseViewModel<BaseNavigator>>(),
    BaseNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val productList = dataList?.items
        productList?.let {
            val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            viewDataBinding.recyclerViewProducts.layoutManager = gridLayoutManager
            viewDataBinding.recyclerViewProducts.adapter = ProductsAdapter(productList){
                openActivity(this@ViewAllSearchProductsActivity,
                    ProductDetailsActivity::class.java,
                    Bundle().apply {
                        putParcelable(
                            Constants.PRODUCT,
                            ProductData(it)
                        )
                    })
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
        return R.layout.activity_view_all_search_products
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): BaseViewModel<BaseNavigator>  {
        val viewModel: BaseViewModel<BaseNavigator>  by viewModels()
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