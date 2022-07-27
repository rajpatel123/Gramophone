package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.product.adapter.SimpleListViewAdapter
import agstack.gramophone.ui.home.view.fragments.market.model.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewList
import agstack.gramophone.ui.home.view.fragments.market.model.ReviewListItem
import agstack.gramophone.ui.offer.OfferDetailActivity
import agstack.gramophone.utils.Constants
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class ProductReviewsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductReviewsNavigator>() {

    var mProductReviewDataBundle = ObservableField<GpApiResponseData?>()
    var sortByKey :String=Constants.RECENT
    lateinit var sortByList: ArrayList<String>
    var mProductReviewsList: MutableList<ReviewListItem?>? = null
    private var loadProductOffersDataJob: Job? = null

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA) != null) {

            mProductReviewDataBundle.set(bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA))
            getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.ratingandreviews)!!)

            var mProductReviewsList: List<ReviewListItem?>? = mProductReviewDataBundle?.get()?.reviewList?.data


            getNavigator()?.setProductReviewsAdapter(RatingAndReviewsAdapter(mProductReviewsList))


        }
    }

    fun setSortingListOptions(list: ArrayList<String>) {
        sortByList = list
        //set sortByList
        getNavigator()?.setSortBySpinnerAdapter(SimpleListViewAdapter(sortByList)) {SortByValue->
            if(SortByValue.equals(Constants.RECENT,true)){
                sortByKey= Constants.RECENT
            } else if(SortByValue.equals(Constants.TOP,true)){
            sortByKey= Constants.TOP
        }
            getNavigator()?.hideDropDown()
            viewModelScope.launch {
            loadProductOffersDataJob.cancelIfActive()
            loadProductOffersDataJob = checkNetworkThenRun {
                val ReviewsResponse = productRepository.getProductReviewsData(sortByKey,
                    "1", ProductData(mProductReviewDataBundle.get()?.selfRating?.productId!!))
                //show loader
                mProductReviewsList?.clear()
                mProductReviewsList?.addAll(ReviewsResponse.body()?.gpApiResponseData?.reviewList?.data!!)



            }}


        }
    }


    private fun checkNetworkThenRun(runCode: (suspend () -> Unit)): Job {
        return viewModelScope.launch {
            try {
                if (getNavigator()?.isNetworkAvailable() == true) {
                    runCode.invoke()
                } else {
                    getNavigator()?.showToast(R.string.nointernet)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    val loadMore: (Int, (() -> Unit)?) -> Unit = { current_page, action ->

        loadProductOffersDataJob.cancelIfActive()
        //if current page =1, Clear the list sent to adapter
        if(current_page==1){
            mProductReviewsList?.clear()
        }
        loadProductOffersDataJob = viewModelScope.launch {
            // add this code on ScrollUp of recycler View with Pgno = Pgno+1
            val productReviewsResponseDeferred = async {
                productRepository.getProductReviewsData(sortByKey, current_page.toString(), ProductData(mProductReviewDataBundle.get()?.selfRating?.productId!!)
                )
            }

            val ReviewsResponse = productReviewsResponseDeferred.await()


        }


    }





}
