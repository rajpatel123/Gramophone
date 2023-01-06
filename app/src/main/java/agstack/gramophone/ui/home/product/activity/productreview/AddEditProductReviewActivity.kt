package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ProductReviewDialogBinding
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import com.amnix.xtension.extensions.enableIf
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
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
            var rating = rating.toDouble()
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
        sendReviewMoEngageEvent(true)
        finish()
    }

    override fun finishActivityandRefreshProductDetails(b: Boolean) {
        sendReviewMoEngageEvent(false)
        SharedPreferencesHelper.instance?.putBoolean(SharedPreferencesKeys.IS_GENUENE, b)
        finish()
        //write setResult functionality here

    }

    private fun sendReviewMoEngageEvent(isCancelled: Boolean) {
        val properties = Properties()
        properties.addAttribute("Product_Id", addEditProductReviewViewModel.productId)
            .addAttribute("Product_Base_Name", addEditProductReviewViewModel.productBaseName.get())
            .addAttribute("App Version", BuildConfig.VERSION_NAME)
            .addAttribute("SDK Version", Build.VERSION.SDK_INT)
            .setNonInteractive()
        if (isCancelled) {
            MoEAnalyticsHelper.trackEvent(this, "KA_Write_Product_Review_Cancel", properties)
        } else {
            MoEAnalyticsHelper.trackEvent(this, "KA_Product_Review_Submitted", properties)
        }

    }

}
