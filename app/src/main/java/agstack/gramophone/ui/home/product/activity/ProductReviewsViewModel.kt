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
import okhttp3.internal.notify
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class ProductReviewsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductReviewsNavigator>() {

    var mProductReviewDataBundle = ObservableField<GpApiResponseData?>()
    var sortByKey: String = Constants.RECENT
    var sortBySpinnerText = ObservableField<String>()
    lateinit var sortByList: ArrayList<String>
    var mProductReviewsList = ArrayList<ReviewListItem?>()
    private var loadProductOffersDataJob: Job? = null
    var isLastPage = false

    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA) != null) {

            mProductReviewDataBundle.set(bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA))
            getNavigator()?.setToolbarTitle(getNavigator()?.getMessage(R.string.ratingandreviews)!!)
            sortBySpinnerText.set(getNavigator()?.getMessage(R.string.top_reviews)!!)

           mProductReviewsList=
                (mProductReviewDataBundle?.get()?.reviewList?.data) as ArrayList<ReviewListItem?>


            getNavigator()?.setProductReviewsAdapter(RatingAndReviewsAdapter(mProductReviewsList))


        }
    }

    fun setSortingListOptions(list: ArrayList<String>) {
        sortByList = list
        //set sortByList
        getNavigator()?.setSortBySpinnerAdapter(SimpleListViewAdapter(sortByList)) { SortByValue ->
            if (SortByValue.equals(Constants.RECENT_REVIEWS, true)) {
                sortByKey = Constants.RECENT
            } else if (SortByValue.equals(Constants.TOP_REVIEWS, true)) {
                sortByKey = Constants.TOP
            }
            sortBySpinnerText.set(SortByValue)

            getNavigator()?.hideDropDown()
            viewModelScope.launch {
                loadProductOffersDataJob.cancelIfActive()
                loadProductOffersDataJob = checkNetworkThenRun {
                    try{
                    val ReviewsResponse = productRepository.getProductReviewsData(
                        sortByKey,
                        "1", ProductData(mProductReviewDataBundle.get()?.selfRating?.productId!!)
                    )

                        //for page 1, clear the list and load from starting
                        mProductReviewsList?.clear()
                        getNavigator()?.resetListPosition()
                        isLastPage = false
                        mProductReviewsList.addAll(ReviewsResponse.body()?.gpApiResponseData?.reviewList?.data!!)
                        getNavigator()?.notifyDataSetChanged()

                    }catch (e:Exception){
                        e.printStackTrace()
                    }




                }
            }


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

    fun loadMore(currentPage:Int) {
        loadProductOffersDataJob.cancelIfActive()
        getNavigator()?.showLoaderFooter()

        loadProductOffersDataJob = viewModelScope.launch {

            val ReviewsResponse = productRepository.getProductReviewsData(
                sortByKey,
                currentPage.toString(),
                ProductData(mProductReviewDataBundle.get()?.selfRating?.productId!!)
            )

            if (ReviewsResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                mProductReviewsList.addAll(ReviewsResponse.body()?.gpApiResponseData?.reviewList?.data!!)
                val lastpage = ReviewsResponse.body()?.gpApiResponseData?.reviewList?.meta?.to
                if (currentPage == lastpage) {
                    isLastPage = true

                }
                getNavigator()?.onListUpdated()
            }else{
                getNavigator()?.onListUpdated()
            }

        }
    }


}



