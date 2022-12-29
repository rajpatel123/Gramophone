package agstack.gramophone.ui.home.product.activity.productreview

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface AddEditProductReviewNavigator :BaseNavigator {
    fun getBundle(): Bundle?
    fun finishActivityandRefreshProductDetails(b: Boolean)
    fun enableSubmitButton(enableSubmit: Boolean)
}