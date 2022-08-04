package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductReviewDialogBinding
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
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
        viewDataBinding?.ratingbarUser?.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {

               // how to get rating value?

            }
            return@OnTouchListener true
        })
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

}
