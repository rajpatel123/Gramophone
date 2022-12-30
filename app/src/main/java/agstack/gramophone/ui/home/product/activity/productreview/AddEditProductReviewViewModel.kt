package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.R
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.SelfRating
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.NonNullObservableField
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.amnix.xtension.extensions.isNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class AddEditProductReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<AddEditProductReviewNavigator>() {

     var productId : Int?=null
    var productBaseName = ObservableField<String>()
    var productRating = ObservableField<Double>()
    var productRatingData = ObservableField<SelfRating>()
    var customerReviewText = ObservableField<String>()
    var isSubmitReviewEnabled = NonNullObservableField<Boolean>(false)
    private var updateReviewJob: Job? = null
    private var addReviewJob: Job? = null
    var progressLoader = ObservableField<Boolean>(false)


    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        /* putInt(Constants.Product_Id_Key, productId)*/
        if(bundle?.getInt(Constants.Product_Id_Key)!=null){
            productId=bundle?.getInt(Constants.Product_Id_Key)
        }

        if (bundle?.getString(Constants.Product_Base_Name) != null) {
            productBaseName.set(bundle?.getString(Constants.Product_Base_Name))
        }
        if (bundle?.getDouble(Constants.RATING_SELECTED) != null) {
            productRating.set(bundle?.getDouble(Constants.RATING_SELECTED))
        }
        if (bundle?.getParcelable<SelfRating>(Constants.PRODUCT_RATING_DATA_KEY) != null) {
            val bundleProductRatingData =
                bundle?.getParcelable<SelfRating>(Constants.PRODUCT_RATING_DATA_KEY)
            productRatingData.set(bundleProductRatingData as SelfRating)



            bundleProductRatingData.comment?.let {

                customerReviewText.set(bundleProductRatingData.comment)
                isSubmitReviewEnabled.set(true)

            }

        }


    }

    fun onReviewTextChanged() {
        if (customerReviewText.get()!!.isNotEmpty() && customerReviewText.get()!!.length > 0) {
            isSubmitReviewEnabled.set(true)
            getNavigator()?.enableSubmitButton(true)
        } else {
            isSubmitReviewEnabled.set(false)
            getNavigator()?.enableSubmitButton(false)
        }



    }

    fun onCrossClicked() {
        getNavigator()?.finishActivity()
    }

    fun onSubmitClick() {
      try {
          var rating = productRating.get()
          var customerReview = customerReviewText.get()
          if (productRatingData.get()?.isNull() == true) {
              //Means data from bundle had no comments before

              addReviewJob.cancelIfActive()
              addReviewJob = checkNetworkThenRun {
                  progressLoader.set(true)
                  var productDetailstoBeAdded = ProductData()

                  // productDetailstoBeAdded.product_id = productRatingData?.get()?.productId!!
                  productDetailstoBeAdded.product_id = productId
                  productDetailstoBeAdded.rating = rating
                  productDetailstoBeAdded.comment = customerReview
                  val addTocartResponse =
                      productRepository.addProductReviewsData(productDetailstoBeAdded)

                  if (addTocartResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                      progressLoader.set(false)
                      getNavigator()?.finishActivityandRefreshProductDetails(false)
                  } else {
                      progressLoader.set(false)
                      getNavigator()?.finishActivityandRefreshProductDetails(true)
                      //getNavigator()?.showToast(addTocartResponse.message())
                  }
              }
          } else if (rating != null && rating > 0) {
              // check only on rating because button will only be enabled if text is not null
              updateReviewJob.cancelIfActive()
              updateReviewJob = checkNetworkThenRun {
                  progressLoader.set(true)
                  var productDetailstoBeUpdated = ProductData()
                  productDetailstoBeUpdated.product_id = productId
                  productDetailstoBeUpdated.rating = rating
                  productDetailstoBeUpdated.comment = customerReview
                  val addTocartResponse =
                      productRepository.updateProductReviewsData(productDetailstoBeUpdated)

                  if (addTocartResponse.body()?.gpApiStatus.equals(Constants.GP_API_STATUS)) {
                      progressLoader.set(false)
                      getNavigator()?.finishActivityandRefreshProductDetails(false)
                  } else {
                      progressLoader.set(false)
                      getNavigator()?.finishActivityandRefreshProductDetails(true)
                      //getNavigator()?.showToast(addTocartResponse.message())
                  }
              }
          }

      }catch (ex:Exception){
          ex.printStackTrace()
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
}