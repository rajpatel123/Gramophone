package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductDetailBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductAllReviewsActivity :
    BaseActivityWrapper<ProductDetailBinding, ProductReviewsNavigator, ProductReviewsViewModel>(),
    ProductReviewsNavigator {


    private val productReviewsViewModel: ProductReviewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productReviewsViewModel.getBundleData()
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

}
