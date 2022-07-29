package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.market.model.SelfRating
import agstack.gramophone.utils.Constants
import android.util.Log
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddEditProductReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<AddEditProductReviewNavigator>() {

    var productBaseName=ObservableField<String>()
    var productRating=ObservableField<Double>()
    var productRatingData = ObservableField<SelfRating>()
    var customerReviewText = ObservableField<String>()


    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if(bundle?.getString(Constants.Product_Base_Name)!=null){
            productBaseName.set(bundle?.getString(Constants.Product_Base_Name))
        }
        if (bundle?.getParcelable<SelfRating>(Constants.PRODUCT_RATING_DATA_KEY) != null) {
            val bundleProductRatingData = bundle?.getParcelable<SelfRating>(Constants.PRODUCT_RATING_DATA_KEY)
            productRatingData.set(bundleProductRatingData  as SelfRating)
            productRating.set(bundleProductRatingData.rating)
            customerReviewText.set(bundleProductRatingData.comment)

        }


    }

    fun onCrossClicked() {
        getNavigator()?.finishActivity()
    }

    fun onSubmitClick(){
        var rating=productRating.get()
        var customerReview = customerReviewText.get()
        //Call submit Review API and refresh previous page
    }
}