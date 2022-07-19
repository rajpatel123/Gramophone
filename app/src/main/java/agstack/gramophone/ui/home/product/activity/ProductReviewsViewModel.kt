package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.data.repository.product.ProductRepository
import agstack.gramophone.ui.home.view.fragments.market.model.GpApiResponseData
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject




@HiltViewModel
class ProductReviewsViewModel  @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel<ProductReviewsNavigator>() {


    fun getBundleData() {
        val bundle = getNavigator()?.getBundle()
        if (bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA) != null) {
            val mProductReviewData = (bundle?.getParcelable<GpApiResponseData>(Constants.PRODUCTREVIEWDATA))
            getNavigator()?.setToolbarTitle("Rating and Reviews")

        }
    }

}
