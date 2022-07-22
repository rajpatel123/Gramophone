package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductAllReviewsActivityBinding
import agstack.gramophone.ui.home.product.adapter.SimpleListViewAdapter
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_all_reviews_activity.*


@AndroidEntryPoint
class ProductAllReviewsActivity :
    BaseActivityWrapper<ProductAllReviewsActivityBinding, ProductReviewsNavigator, ProductReviewsViewModel>(),
    ProductReviewsNavigator {


    private val productReviewsViewModel: ProductReviewsViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productReviewsViewModel.getBundleData()

        setAutoCompleteSortByAdapterList()
    }



    private fun setAutoCompleteSortByAdapterList() {
        var sortByListOptions: Array<String> = resources.getStringArray(R.array.SortReviewsByOptions)
        mViewModel?.sortByList = sortByListOptions.toCollection(ArrayList())
        mViewModel?.setSortingListOptions(sortByListOptions.toCollection(ArrayList()))
        /*var sortByAdapter= ArrayAdapter<String>(this, android.R.layout.select_dialog_item , sortByListOptions)
        viewDataBinding?.autocompleteSortBy?.setAdapter(sortByAdapter)
        sortByAdapter.notifyDataSetChanged()
      autocomplete_sortBy.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                autocomplete_sortBy.showDropDown()
                return true
            } })



        viewDataBinding?.autocompleteSortBy?.setOnItemClickListener { parent, view, position, id ->

            Log.d("ItemSelectedValue", sortByListOptions[position])
        }*/

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
        autocomplete_sortBy.setAdapter(simpleListViewAdapter)
        simpleListViewAdapter.notifyDataSetChanged()
    }

    override fun setProductReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter) {
        viewDataBinding?.rvReviewsProduct?.adapter= ratingAndReviewsAdapter
    }

}
