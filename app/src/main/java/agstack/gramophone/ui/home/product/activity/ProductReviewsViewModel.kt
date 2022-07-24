package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.product.adapter.SimpleListViewAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewList
import agstack.gramophone.utils.Constants
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductReviewsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductReviewsNavigator>() {
    var mProductReviewDataBundle = ObservableField<GpApiResponseData?>()

    //var sortByList = arrayListOf<String>(getNavigator()?.getMessage(R.string.top_reviews)!!,getNavigator()?.getMessage(R.string.recently_viewed)!!)
    lateinit var sortByList: ArrayList<String>
    var mProductReviewsList: ReviewList? = null

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA) != null) {

            mProductReviewDataBundle.set(bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA))
            getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.ratingandreviews)!!)

            mProductReviewsList = mProductReviewDataBundle?.get()?.reviewList


            getNavigator()?.setProductReviewsAdapter(RatingAndReviewsAdapter(mProductReviewsList))


        }
    }

    fun setSortingListOptions(list: ArrayList<String>) {
        sortByList = list
        //set sortByList
        getNavigator()?.setSortBySpinnerAdapter(SimpleListViewAdapter(sortByList)) {

            //Clear the list sent to adapter
            viewModelScope.launch {
                // add this code on ScrollUp of recycler View with Pgno = Pgno+1
                val productReviewsResponseDeferred = async {
                    productRepository.getProductReviewsData(
                        Constants.RECENT,
                        "1",
                        ProductData(mProductReviewDataBundle.get()?.selfRating?.productId!!)
                    )
                }

                val ReviewsResponse = productReviewsResponseDeferred.await()
            }

        }
    }

}
