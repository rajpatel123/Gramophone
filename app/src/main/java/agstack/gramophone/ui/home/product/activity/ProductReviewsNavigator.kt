package agstack.gramophone.ui.home.product.activity

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface ProductReviewsNavigator :BaseNavigator {
    fun getBundle(): Bundle?
     fun setToolbarTitle(title: String)
}