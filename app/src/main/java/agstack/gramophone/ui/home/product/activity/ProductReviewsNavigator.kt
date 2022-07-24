package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.home.product.adapter.SimpleListViewAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewListItem
import android.os.Bundle

interface ProductReviewsNavigator :BaseNavigator {
    fun getBundle(): Bundle?
     fun setToolbarTitle(title: String)
    fun setProductReviewsAdapter(ratingAndReviewsAdapter: RatingAndReviewsAdapter)
    fun setSortBySpinnerAdapter(simpleListViewAdapter: SimpleListViewAdapter, onItemClick: (String) -> Unit)

}