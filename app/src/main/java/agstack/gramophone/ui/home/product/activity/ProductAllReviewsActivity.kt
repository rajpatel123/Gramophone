package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductAllReviewsActivityBinding
import agstack.gramophone.ui.home.product.adapter.SimpleListViewAdapter
import agstack.gramophone.utils.EndlessRecyclerScrollListener
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_all_reviews_activity.*


@AndroidEntryPoint
class ProductAllReviewsActivity :
    BaseActivityWrapper<ProductAllReviewsActivityBinding, ProductReviewsNavigator, ProductReviewsViewModel>(),
    ProductReviewsNavigator {


    private val productReviewsViewModel: ProductReviewsViewModel by viewModels()
    private lateinit var listener: EndlessRecyclerScrollListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productReviewsViewModel.getBundleData()
        setAutoCompleteSortByAdapterList()

    }

    override fun notifyDataSetChanged() {
        viewDataBinding.rvReviewsProduct.adapter?.notifyDataSetChanged()
    }

    override fun onListUpdated() {
        listener.onListFetched()
        (viewDataBinding.rvReviewsProduct.adapter as RatingAndReviewsAdapter).hideLoadingItem()
        viewDataBinding.rvReviewsProduct.adapter?.notifyDataSetChanged()
    }

    private fun setScrollListenerOnReviewsList() {
        listener = object : EndlessRecyclerScrollListener(
            viewDataBinding.rvReviewsProduct.layoutManager as LinearLayoutManager,
            { mViewModel?.loadMore(it) }) {
            override fun isLastPage(): Boolean {
                return mViewModel?.isLastPage ?: false
            }

        }
        viewDataBinding.rvReviewsProduct.addOnScrollListener(listener)

    }

    override fun showLoaderFooter() {
        (viewDataBinding.rvReviewsProduct.adapter as RatingAndReviewsAdapter).showLoadingItem()
        viewDataBinding.rvReviewsProduct.adapter?.notifyDataSetChanged()
    }


    private fun setAutoCompleteSortByAdapterList() {
        var sortByListOptions: Array<String> =
            resources.getStringArray(R.array.SortReviewsByOptions)
        mViewModel?.sortByList = sortByListOptions.toCollection(ArrayList())
        mViewModel?.setSortingListOptions(sortByListOptions.toCollection(ArrayList()))

    }


    override fun getLayoutID(): Int {
        return R.layout.product_all_reviews_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ProductReviewsViewModel {
        return productReviewsViewModel
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_arrow_left)
    }

    override fun setSortBySpinnerAdapter(
        simpleListViewAdapter: SimpleListViewAdapter,
        onItemClick: (String) -> Unit
    ) {
        simpleListViewAdapter.OnItemSelectedListener(onItemClick)
        viewDataBinding.autocompleteSortBy.setAdapter(simpleListViewAdapter)
        simpleListViewAdapter.notifyDataSetChanged()
        viewDataBinding.autocompleteSortBy.setOnClickListener { autocomplete_sortBy.showDropDown() }

    }

    override fun hideDropDown() {

        viewDataBinding.autocompleteSortBy.dismissDropDown()
    }

    override fun setProductReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter) {
        viewDataBinding?.rvReviewsProduct?.adapter = ratingAndReviewsAdapter
        setScrollListenerOnReviewsList()
    }

}
