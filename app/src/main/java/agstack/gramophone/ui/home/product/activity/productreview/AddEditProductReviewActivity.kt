package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductReviewDialogBinding
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import com.amnix.xtension.extensions.enableIf
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEditProductReviewActivity :
    BaseActivityWrapper<ProductReviewDialogBinding, AddEditProductReviewNavigator, AddEditProductReviewViewModel>(),
    AddEditProductReviewNavigator {
    private val addEditProductReviewViewModel: AddEditProductReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.getBundleData()
        setRatingListener()
    }

    private fun setRatingListener() {

        viewDataBinding.ratingbarUser.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            var rating =  rating.toDouble()
            mViewModel?.productRating?.set(rating)



        }



    }


    override fun enableSubmitButton(enableSubmit: Boolean) {
        viewDataBinding.btnSubmit.enableIf(enableSubmit)
    }
    override fun getBundle(): Bundle? {
        return intent.extras
    }



    override fun getLayoutID(): Int {
        return R.layout.product_review_dialog
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AddEditProductReviewViewModel {
        return addEditProductReviewViewModel
    }


    override fun finishActivity() {
        finish()
    }

    override fun finishActivityandRefreshProductDetails(){
        finish()
        //write setResult functionality here

    }

}
